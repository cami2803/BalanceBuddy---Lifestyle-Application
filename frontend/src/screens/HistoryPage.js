import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Calendar from '../components/Calendar';
import styles from '../styles/HistoryStyle';
import API_BASE_URL from '../utils/environment_variables';
import AsyncStorage from '@react-native-async-storage/async-storage';

const HistoryPage = ({ navigation }) => {
    const [selectedDate, setSelectedDate] = useState(null);
    const [dailyProgressPercentage, setDailyProgressPercentage] = useState(null); 
    const [userId, setUserId] = useState(null);
    const [errorMessage, setErrorMessage] = useState(''); // State for error messages

    useEffect(() => {
        const fetchUserId = async () => {
            try {
                const accessToken = await AsyncStorage.getItem('accessToken');
                const response = await fetch(`${API_BASE_URL}/user/me`, {
                    method: 'GET',
                    headers: { 'Authorization': `Bearer ${accessToken}` }
                });
                const data = await response.json();
                setUserId(data.id);
            } catch (error) {
                console.error('Error fetching user ID:', error);
                setErrorMessage('Unable to fetch user ID.'); // Handle user ID fetch error
            }
        };
    
        fetchUserId();
    }, []);
    
    useEffect(() => {
        if (selectedDate && userId) {
            const fetchProgress = async () => {
                try {
                    const accessToken = await AsyncStorage.getItem('accessToken');
                    const formattedDate = new Date(selectedDate).toISOString().split('T')[0];
                    const response = await fetch(`${API_BASE_URL}/history/${userId}/${formattedDate}`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${accessToken}`
                        },
                    });
    
                    if (response.ok) {
                        const historyData = await response.json();
                        setDailyProgressPercentage(`${historyData.percentage}%`);
                        setErrorMessage(''); // Clear any previous error messages
                    } else {
                        setDailyProgressPercentage(null);
                        setErrorMessage('No progress data found for the selected date.');
                    }
                } catch (error) {
                    console.error('Error fetching progress:', error);
                    setDailyProgressPercentage(null);
                    setErrorMessage('Unable to fetch progress. Please try again later.');
                }
            };
    
            fetchProgress();
        }
    }, [selectedDate, userId]);
    

    const markedDates = {
        '2024-09-01': { color: '#FF9999' }, // daily
        '2024-09-02': { color: '#99FF99' }, // weekly
        '2024-09-10': { color: '#9999FF' }  // monthly
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>History</Text>
            <Calendar
                onDayPress={(day) => setSelectedDate(day)}
                markedDates={markedDates}
                selectedDate={selectedDate}
            />
            <View>
                {dailyProgressPercentage !== null ? (
                    <Text style={styles.progressText}>Progress: {dailyProgressPercentage}</Text>
                ) : (
                    <Text style={styles.progressText}>{errorMessage || 'No progress data found for the selected date.'}</Text>
                )}
            </View>
            <View style={styles.colorLegendContainer}>
                <Text style={styles.legendTitle}>Color Legend</Text>
                <View style={styles.legendItem}>
                    <View style={[styles.legendColorBox, { backgroundColor: '#FF9999' }]} />
                    <Text style={styles.legendText}>Daily Goal</Text>
                </View>
                <View style={styles.legendItem}>
                    <View style={[styles.legendColorBox, { backgroundColor: '#99FF99' }]} />
                    <Text style={styles.legendText}>Weekly Goal</Text>
                </View>
                <View style={styles.legendItem}>
                    <View style={[styles.legendColorBox, { backgroundColor: '#9999FF' }]} />
                    <Text style={styles.legendText}>Monthly Goal</Text>
                </View>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('HomePage')}
                >
                    <Text style={styles.buttonText}>Back to Home</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

export default HistoryPage;
