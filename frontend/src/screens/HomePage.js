import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const HomePage = ({ navigation }) => {
    const handleLogout = async () => {
        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            const response = await fetch('http://10.0.2.2:8080/api/auth/logout', { // emulator
            //const response = await fetch('http://192.168.1.130:8080/api/auth/logout', { // phone
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
            });
            if (response.ok) {
                const contentType = response.headers.get('Content-Type');
                if (contentType && contentType.includes('application/json')) {
                    const data = await response.json(); 
                    Alert.alert("Success", data.message || "You have been logged out.");
                } else {
                    Alert.alert("Success", "You have been logged out.");
                }
                await AsyncStorage.removeItem('accessToken');
                navigation.navigate('LoginPage');
            } else {
                const errorData = await response.json().catch(() => ({}));
                Alert.alert("Error", errorData.error || "Logout failed");
            }
        } catch (error) {
            Alert.alert("Error", "An error occurred while logging out");
        }
    };

    return (
        <View style={styles.container}>
            <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
                <Text style={styles.logoutButtonText}>Logout</Text>
            </TouchableOpacity>
            <Text style={styles.title}>Home Page</Text>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('GoalPage')}>
                    <Text style={styles.buttonText}>See Goal</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('HabitsPage')}>
                    <Text style={styles.buttonText}>See Habits</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('HistoryPage')}>
                    <Text style={styles.buttonText}>See History</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('LastReportPage')}>
                    <Text style={styles.buttonText}>See Last Report</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('SettingsPage')}>
                    <Text style={styles.buttonText}>Settings</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#573d3c',
        marginBottom: 20,
    },
    buttonContainer: {
        width: '80%',
        marginTop: 20,
    },
    button: {
        backgroundColor: '#f4f3ee',
        padding: 15,
        borderRadius: 5,
        alignItems: 'center',
        marginBottom: 10,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
    logoutButton: {
        position: 'absolute',
        top: 20,
        right: 20,
        backgroundColor: '#f4f3ee',
        padding: 10,
        borderRadius: 5,
    },
    logoutButtonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default HomePage;
