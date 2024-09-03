import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity } from 'react-native';
import useAuthFetch from '../utils/useAuthFetch';
import { Picker } from '@react-native-picker/picker';
import styles from '../styles/NewGoalStyle';
import API_BASE_URL from '../utils/environment_variables';

const NewGoalPage = ({ navigation }) => {
    const [habits, setHabits] = useState([]);
    const [selectedHabits, setSelectedHabits] = useState([]);
    const [target, setTarget] = useState('');
    const [periodicity, setPeriodicity] = useState('');
    const [isCreating, setIsCreating] = useState(false);
    const [selectedHabitID, setSelectedHabitID] = useState(null);
    const [selectedUnit, setSelectedUnit] = useState('');
    const { fetchWithAuth } = useAuthFetch();
    const [userID, setUserID] = useState(null);

    useEffect(() => {
        fetchWithAuth(`${API_BASE_URL}/habits`)
            .then(data => {
                if (Array.isArray(data)) {
                    setHabits(data);
                } else {
                    console.error('Expected array of habits but received:', data);
                }
            })
            .catch(error => {
                console.error('Error fetching habits:', error);
            });
    }, [fetchWithAuth]);

    useEffect(() => {
        fetchWithAuth(`${API_BASE_URL}/user/me`)
            .then(userData => {
                if (userData && userData.id) {
                    setUserID(userData.id);
                }
            })
            .catch(error => {
                console.error('Error fetching user ID:', error);
            });
    }, [fetchWithAuth]);

    const handleAddHabit = () => {
        if (selectedHabitID && target) {
            const habitToAdd = habits.find(habit => habit.habitID === selectedHabitID);
            if (habitToAdd && !selectedHabits.some(h => h.habitID === habitToAdd.habitID)) {
                setSelectedHabits(prevHabits => [...prevHabits, { ...habitToAdd, target }]);
                setTarget('');
                setSelectedHabitID(null);
                setSelectedUnit('');
            }
        }
    };

    const handleCreateGoal = () => {
        setIsCreating(true);
    };

    const handleSaveGoal = () => {
        if (userID === null) {
            console.error('User ID is not available');
            return;
        }

        const habitNames = selectedHabits.map(habit => habit.name).join(';');
        const habitTargets = selectedHabits.map(habit => habit.target).join(';');

        const goalRequest = {
            userID,
            periodicity,
            target: habitTargets,
            habits: habitNames, 
        };

        fetchWithAuth(`${API_BASE_URL}/goals`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(goalRequest),
        })
            .then(data => {
                console.log('Goal created:', data);
                setSelectedHabits([]);
                setTarget('');
                setPeriodicity('');
                setIsCreating(false);
            })
            .catch(error => {
                console.error('Error creating goal:', error);
            });
    };

    const handleHabitChange = (habitID) => {
        setSelectedHabitID(habitID);
        const habit = habits.find(h => h.habitID === habitID);
        if (habit) {
            setSelectedUnit(habit.unit);
        } else {
            setSelectedUnit('');
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Create New Goal</Text>
            <View style={styles.section}>
                <Picker
                    selectedValue={selectedHabitID}
                    style={styles.picker}
                    onValueChange={handleHabitChange}
                >
                    <Picker.Item label="Select a Habit" value={null} />
                    {habits.map(habit => (
                        <Picker.Item key={habit.habitID} label={habit.name} value={habit.habitID} />
                    ))}
                </Picker>
            </View>
            <View style={styles.section}>
                <Text>Target Value:</Text>
                <TextInput
                    style={styles.input}
                    value={target}
                    onChangeText={setTarget}
                    keyboardType="numeric"
                />
                {selectedUnit && <Text style={styles.unitText}>{`(${selectedUnit})`}</Text>}
            </View>
            <TouchableOpacity style={styles.button} onPress={handleAddHabit}>
                <Text style={styles.buttonText}>Add Habit to Goal</Text>
            </TouchableOpacity>
            <View style={styles.section}>
                <Text style={styles.subtitle}>Number of Habits in Goal: {selectedHabits.length}</Text>
            </View>
            <TouchableOpacity style={styles.button} onPress={handleCreateGoal}>
                <Text style={styles.buttonText}>Create Goal</Text>
            </TouchableOpacity>
            {isCreating && (
                <View style={styles.section}>
                    <Text style={styles.subtitle}>Select Periodicity</Text>
                    <Picker
                        selectedValue={periodicity}
                        onValueChange={(itemValue) => setPeriodicity(itemValue)}
                    >
                        <Picker.Item label="Select Periodicity" value="" />
                        <Picker.Item label="DAILY" value="DAILY" />
                        <Picker.Item label="WEEKLY" value="WEEKLY" />
                        <Picker.Item label="MONTHLY" value="MONTHLY" />
                        <Picker.Item label="ANNUALY" value="ANNUALY" />
                    </Picker>
                    <TouchableOpacity style={styles.button} onPress={handleSaveGoal}>
                        <Text style={styles.buttonText}>Save Goal</Text>
                    </TouchableOpacity>
                </View>
            )}
            <TouchableOpacity
                style={styles.button}
                onPress={() => navigation.navigate('CreateHabitPage')}
            >
                <Text style={styles.buttonText}>Create a New Habit</Text>
            </TouchableOpacity>
        </View>
    );
};

export default NewGoalPage;
