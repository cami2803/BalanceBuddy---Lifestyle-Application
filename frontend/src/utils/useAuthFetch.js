import { useState, useCallback } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useNavigation } from '@react-navigation/native';
import API_BASE_URL from './environment_variables';

const useAuthFetch = () => {
    const [loading, setLoading] = useState(false);
    const navigation = useNavigation();

    const fetchWithAuth = useCallback(async (url, options = {}) => {
        setLoading(true);
        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            if (!accessToken) throw new Error('No access token available');
    
            let response = await fetch(url, {
                ...options,
                headers: {
                    ...options.headers,
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`,
                },
            });

            if (response.status === 401) {
                console.log('Access token expired. Renewing...');
                const newAccessToken = await renewAccessToken();
                if (newAccessToken) {
                    console.log('Retrying request with new access token');
                    response = await fetch(url, {
                        ...options,
                        headers: {
                            ...options.headers,
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${newAccessToken}`,
                        },
                    });
                } else {
                    throw new Error('Failed to renew access token');
                }
            }
    
            if (response.status === 204 || (response.status === 200 && response.headers.get('Content-Length') === '0')) {
                return null;
            }

            const contentType = response.headers.get('Content-Type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }

            // if body response is empty content
            const responseText = await response.text();
            if (!responseText) {
                return null;
            }
            return responseText;
            
        } catch (error) {
            console.error('Error fetching data:', error);
            if (error.message.includes('Failed to renew access token')) {
                navigation.navigate('LoginPage');
            }
            return { error: error.message };
        } finally {
            setLoading(false);
        }
    }, [navigation]);


    const renewAccessToken = useCallback(async () => {
        try {
            const refreshToken = await AsyncStorage.getItem('refreshToken');
            if (!refreshToken) throw new Error('No refresh token available');
            
            const response = await fetch(`${API_BASE_URL}/auth/token`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ refreshToken }),
            });

            if (response.ok) {
                const newTokens = await response.json();
                await AsyncStorage.setItem('accessToken', newTokens.accessToken);
                return newTokens.accessToken;
            } else {
                console.error('Failed to renew token');
                return null;
            }
        } catch (error) {
            console.error('Error renewing access token:', error);
            return null;
        }
    }, []);

    return { fetchWithAuth, loading };
};

export default useAuthFetch;
