import React, { useEffect, useState } from 'react';
import { Text, TextInput, TouchableOpacity, Alert, ScrollView } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import useAuthFetch from '../utils/useAuthFetch';
import styles from '../styles/AddProgressStyle';
import HomePage from './HomePage';

const AddProgressPage = ({ route, navigation }) => {
    const { goalId } = route.params;
    const [habitName, setHabitName] = useState('');
    const [progressValue, setProgressValue] = useState('');
    const [habits, setHabits] = useState([]);
    const { fetchWithAuth } = useAuthFetch();

    useEffect(() => {
        const fetchHabits = async () => {
            try {
                const goalData = await fetchWithAuth(`http://10.0.2.2:8080/api/goals/${goalId}`); // emulator
                // const goalData = await fetchWithAuth('http://192.168.1.130:8080/api/goals/${goalId}'); // phone

                if (goalData && goalData.habits) {
                    const habitsList = goalData.habits.split(';');
                    setHabits(habitsList);
                    if (habitsList.length > 0) {
                        setHabitName(habitsList[0]);
                    }
                }
            } catch (error) {
                console.error('Error fetching goal data:', error);
                Alert.alert('Error', 'Failed to fetch goal data.');
            }
        };

        fetchHabits();
    }, [goalId, fetchWithAuth]);

    const handleUpdateProgress = async () => {
        if (!habitName || !progressValue) {
            Alert.alert('Error', 'Habit name and progress value are required.');
            return;
        }
    
        try {
            const response = await fetchWithAuth(`http://10.0.2.2:8080/api/goals/${goalId}/updateProgress`, {
                method: 'POST',
                body: JSON.stringify({
                    habitName,
                    progressValue: parseInt(progressValue, 10)
                }),
            });
    
            if (response === null || (response.status !== 200 && response.status !== 204)) {
                throw new Error('No response from server');
            }
    
            Alert.alert('Success', 'Progress updated successfully!');
            navigation.navigate(HomePage);
        } catch (error) {
            console.error('Error updating progress:', error);
            Alert.alert('Error', 'Failed to update progress.');
        }
    };
    

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.title}>Update Progress</Text>

            <Text style={styles.label}>Habit Name:</Text>
            <Picker
                selectedValue={habitName}
                style={styles.picker}
                onValueChange={(itemValue) => setHabitName(itemValue)}
            >
                {habits.map((habit, index) => (
                    <Picker.Item key={index} label={habit} value={habit} />
                ))}
            </Picker>

            <Text style={styles.label}>Progress Value:</Text>
            <TextInput
                style={styles.input}
                value={progressValue}
                onChangeText={setProgressValue}
                keyboardType="numeric"
            />

            <TouchableOpacity style={styles.button} onPress={handleUpdateProgress}>
                <Text style={styles.buttonText}>Update Progress</Text>
            </TouchableOpacity>
        </ScrollView>
    );
};

export default AddProgressPage;
