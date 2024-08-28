import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import styles from '../styles/CreateHabitStyle';
import API_BASE_URL from '../utils/environment_variables';

const CreateHabitPage = ({ navigation }) => {
    const [habitName, setHabitName] = useState('');
    const [unit, setUnit] = useState('');

    const handleCreateHabit = async () => {
        if (!habitName || !unit) {
            Alert.alert("Error", "Please fill in all fields.");
            return;
        }

        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            const response = await fetch(`${API_BASE_URL}/habits`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`,
                },
                body: JSON.stringify({
                    name: habitName,
                    unit,
                }),
            });

            if (response.ok) {
                Alert.alert("Success", "Habit created successfully!");
                setHabitName('');
                setUnit('');
                navigation.navigate('EditHabitsPage', { refresh: true });
                navigation.goBack();
            } else {
                const errorData = await response.json().catch(() => ({}));
                Alert.alert("Error", errorData.error || "Failed to create habit");
            }
        } catch (error) {
            Alert.alert("Error", "An error occurred while creating habit");
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Create New Habit</Text>
            <TextInput
                style={styles.input}
                placeholder="Habit Name"
                value={habitName}
                onChangeText={setHabitName}
            />
            <TextInput
                style={styles.input}
                placeholder="Unit of Measurement"
                value={unit}
                onChangeText={setUnit}
            />
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={handleCreateHabit}>
                    <Text style={styles.buttonText}>Create Habit</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

export default CreateHabitPage;
