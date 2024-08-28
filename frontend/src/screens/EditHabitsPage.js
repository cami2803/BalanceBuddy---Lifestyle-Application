import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, TouchableOpacity, FlatList, Alert, TextInput } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';
import styles from '../styles/EditHabitsStyle';
import API_BASE_URL from '../utils/environment_variables';

const EditHabitsPage = ({ navigation, route }) => {
    const [habits, setHabits] = useState([]);
    const [selectedHabit, setSelectedHabit] = useState(null);
    const [newHabitName, setNewHabitName] = useState('');
    const [newUnit, setNewUnit] = useState('');

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

    useEffect(() => {
        if (route.params?.refresh) {
            fetchHabits();
        }
    }, [route.params?.refresh, fetchHabits]);

    const handleUpdateHabit = async () => {
        if (!selectedHabit || !newHabitName || !newUnit) {
            Alert.alert("Error", "Please select a habit and fill in all fields.");
            return;
        }

        const updatedHabit = {
            habitID: selectedHabit.habitID,
            name: newHabitName,
            unit: newUnit,
        };

        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            const response = await fetch(`${API_BASE_URL}/habits/${selectedHabit.habitID}`, {
                method: 'PUT', 
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`,
                },
                body: JSON.stringify(updatedHabit),
            });

            if (response.ok) {
                Alert.alert("Success", "Habit updated successfully!");
                setSelectedHabit('');
                setNewHabitName('');
                setNewUnit('');
                fetchHabits();
            } else {
                const errorData = await response.json().catch(() => ({}));
                Alert.alert("Error", errorData.error || "Failed to update habit");
            }
        } catch (error) {
            Alert.alert("Error", "An error occurred while updating habit");
        }
    };

    const handleDeleteHabit = async () => {
        if (!selectedHabit) {
            Alert.alert("Error", "Please select a habit to delete.");
            return;
        }

        Alert.alert(
            'Confirm Deletion',
            `Are you sure you want to delete the habit "${selectedHabit.name}"?`,
            [
                { text: 'Cancel', style: 'cancel' },
                {
                    text: 'Delete',
                    onPress: async () => {
                        try {
                            const accessToken = await AsyncStorage.getItem('accessToken');
                            const response = await fetch(`${API_BASE_URL}/habits/${selectedHabit.habitID}`, {
                                method: 'DELETE',
                                headers: {
                                    'Authorization': `Bearer ${accessToken}`,
                                },
                            });

                            if (response.ok) {
                                Alert.alert("Success", "Habit deleted successfully!");
                                fetchHabits();
                                setSelectedHabit(null);
                            } else {
                                const errorData = await response.json().catch(() => ({}));
                                Alert.alert("Error", errorData.error || "Failed to delete habit");
                            }
                        } catch (error) {
                            Alert.alert("Error", "An error occurred while deleting habit");
                        }
                    },
                },
            ],
            { cancelable: false }
        );
    };

    const renderHabitItem = ({ item }) => (
        <TouchableOpacity
            style={[
                styles.habitItem,
                selectedHabit && selectedHabit.habitID === item.habitID ? styles.selectedHabitItem : {},
            ]}
            onPress={() => {
                setSelectedHabit(item);
                setNewHabitName(item.name);
                setNewUnit(item.unit);
            }}
        >
            <Text style={styles.habitText}>{item.name} ({item.unit})</Text>
        </TouchableOpacity>
    );

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Edit Habits</Text>
            <FlatList
                data={habits}
                keyExtractor={(item) => item.habitID.toString()} 
                renderItem={renderHabitItem}
                contentContainerStyle={styles.flatListContent}
            />
            {selectedHabit && (
                <>
                    <TextInput
                        style={styles.input}
                        placeholder="New Habit Name"
                        value={newHabitName}
                        onChangeText={setNewHabitName}
                    />
                    <TextInput
                        style={styles.input}
                        placeholder="New Unit of Measurement"
                        value={newUnit}
                        onChangeText={setNewUnit}
                    />
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity style={styles.button} onPress={handleUpdateHabit}>
                            <Text style={styles.buttonText}>Update Habit</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.button} onPress={handleDeleteHabit}>
                            <Text style={styles.buttonText}>Delete Habit</Text>
                        </TouchableOpacity>
                    </View>
                </>
            )}
        </View>
    );
};

export default EditHabitsPage;
