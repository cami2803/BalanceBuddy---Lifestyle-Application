import React, { useState, useEffect } from 'react';
import { View, Text, Switch, TouchableOpacity, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import styles from '../styles/SettingsStyle';
import API_BASE_URL from '../utils/environment_variables';

const SettingsPage = ({ navigation }) => {
    const [dailyReport, setDailyReport] = useState(false); 
    const [habitReminder, setHabitReminder] = useState(false);
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        const fetchSettings = async () => {
            try {
                const accessToken = await AsyncStorage.getItem('accessToken');
                const userResponse = await fetch(`${API_BASE_URL}/user/me`, {
                    headers: { 'Authorization': `Bearer ${accessToken}` }
                });

                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    setUserId(userData.id);

                    const settingsResponse = await fetch(`${API_BASE_URL}/user/notifications/${userData.id}`, {
                        headers: { 'Authorization': `Bearer ${accessToken}` }
                    });

                    if (settingsResponse.ok) {
                        const settings = await settingsResponse.json();

                        setDailyReport(settings.daily === true);
                        setHabitReminder(settings.reminder === true);
                    } else {
                        Alert.alert('Error', 'Failed to fetch settings.');
                    }
                }
            } catch (error) {
                console.error('Error fetching settings:', error);
                Alert.alert('Error', 'An error occurred while fetching settings.');
            }
        };

        fetchSettings();
    }, []);

    const saveSettings = async () => {
        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            const response = await fetch(`${API_BASE_URL}/user/notifications/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    daily: dailyReport ? 1 : 0,
                    reminder: habitReminder ? 1 : 0,
                }),
            });

            if (response.ok) {
                Alert.alert('Success', 'Settings saved successfully!');
            } else {
                Alert.alert('Error', 'Failed to save settings.');
            }
        } catch (error) {
            console.error('Error saving settings:', error);
            Alert.alert('Error', 'An error occurred while saving settings.');
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Settings</Text>

            <View style={styles.settingContainer}>
                <Text style={styles.settingText}>Daily Report Notifications</Text>
                <Switch
                    value={dailyReport}
                    onValueChange={(value) => setDailyReport(value)}
                />
            </View>

            <View style={styles.settingContainer}>
                <Text style={styles.settingText}>Habit Reminders</Text>
                <Switch
                    value={habitReminder}
                    onValueChange={(value) => setHabitReminder(value)}
                />
            </View>

            <TouchableOpacity
                style={styles.button}
                onPress={saveSettings}
            >
                <Text style={styles.buttonText}>Save Settings</Text>
            </TouchableOpacity>

            <TouchableOpacity
                style={styles.button}
                onPress={() => navigation.navigate('AccountPage')}
            >
                <Text style={styles.buttonText}>Account Settings</Text>
            </TouchableOpacity>
        </View>
    );
};

export default SettingsPage;
