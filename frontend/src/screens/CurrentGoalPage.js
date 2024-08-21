import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity } from 'react-native';
import styles from '../styles/CurrentGoalStyle';
import useAuthFetch from '../utils/useAuthFetch';

const CurrentGoalPage = ({ route, navigation }) => {
    const { goalId } = route.params;
    const [goal, setGoal] = useState(null);
    const [habitMap, setHabitMap] = useState({});
    const { fetchWithAuth, loading } = useAuthFetch();

    useEffect(() => {
        const fetchGoal = async () => {
            try {
                const data = await fetchWithAuth(`http://10.0.2.2:8080/api/goals/${goalId}`); //emulator
                // const data = await fetchWithAuth('http://192.168.1.130:8080/api/goals/${goadId}'); // phone
                if (data) {
                    setGoal(data);
                    const habitsData = await fetchWithAuth('http://10.0.2.2:8080/api/habits'); // emulator
                    // const habitsData = await fetchWithAuth('http://192.168.1.130:8080/api/habits'); // phone
                    if (habitsData) {
                        // Create a map of habit name to unit
                        const habitMap = habitsData.reduce((map, habit) => {
                            map[habit.name] = habit.unit;
                            return map;
                        }, {});
                        setHabitMap(habitMap);
                    }
                } else {
                    setGoal(null);
                }
            } catch (error) {
                console.error('Error fetching goal:', error);
                setGoal(null);
            }
        };
        fetchGoal();
    }, [goalId]);

    if (loading) {
        return <Text>Loading...</Text>;
    }

    if (!goal) {
        return <Text>No current goal</Text>;
    }

    const habits = goal.habits ? goal.habits.split(';') : [];
    const targets = goal.target ? goal.target.split(';') : [];
    const progresses = goal.progress ? goal.progress.split(';') : [];

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Current Goal</Text>
            <ScrollView contentContainerStyle={styles.scrollView}>
                <Text style={styles.header}>Habits I want to improve:</Text>
                {habits.length > 0 ? (
                    habits.map((habitName, index) => (
                        <View key={index} style={styles.habitContainer}>
                            <Text style={styles.habitName}>{habitName}</Text>
                            <Text style={styles.item}>
                                Target: {targets[index]} {habitMap[habitName]}
                            </Text>
                            <Text style={styles.item}>
                                Progress: {progresses[index]} {habitMap[habitName]}
                            </Text>
                        </View>
                    ))
                ) : (
                    <Text>No habits listed</Text>
                )}

                <Text style={styles.header}>Periodicity:</Text>
                <Text style={[styles.item, styles.periodicity]}>{goal.periodicity}</Text>


                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('EditGoalPage', { goalId: goal.goalID })}
                >
                    <Text style={styles.buttonText}>Edit Goal</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('AddProgressPage', { goalId: goal.goalID })}
                >
                    <Text style={styles.buttonText}>Add Progress</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('NewGoalPage')}
                >
                    <Text style={styles.buttonText}>Create New Goal</Text>
                </TouchableOpacity>
            </ScrollView>
        </View>
    );
};

export default CurrentGoalPage;
