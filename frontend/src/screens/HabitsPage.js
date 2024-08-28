import React, { useState, useCallback } from 'react';
import { View, Text, TouchableOpacity, FlatList, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';
import styles from '../styles/HabitStyle';
import API_BASE_URL from '../utils/environment_variables';

const HabitsPage = ({ navigation }) => {
    const [habits, setHabits] = useState([]);

    const fetchHabits = useCallback(async () => {
        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            const response = await fetch(`${API_BASE_URL}/habits`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
            });
            if (response.ok) {
                const data = await response.json();
                setHabits(data);
            } else {
                const errorData = await response.json().catch(() => ({}));
                Alert.alert("Error", errorData.error || "Failed to fetch habits");
            }
        } catch (error) {
            Alert.alert("Error", "An error occurred while fetching habits");
        }
    }, []);

    useFocusEffect(
        useCallback(() => {
            fetchHabits();
        }, [fetchHabits])
    );

    const renderHabitItem = ({ item }) => (
        <View style={styles.habitItem}>
            <Text style={styles.habitText}>{item.name} ({item.unit})</Text>
        </View>
    );

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Habits</Text>
            <FlatList
                data={habits}
                keyExtractor={(item, index) => item.id ? item.id.toString() : index.toString()}
                renderItem={renderHabitItem}
                contentContainerStyle={styles.flatListContent}
            />
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('CreateHabitPage')}>
                    <Text style={styles.buttonText}>Create New Habit</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('EditHabitsPage')}>
                    <Text style={styles.buttonText}>Edit Habits</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

export default HabitsPage;
