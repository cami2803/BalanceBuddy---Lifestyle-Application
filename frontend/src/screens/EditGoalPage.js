import React, { useEffect, useState } from 'react';
import { Text, TouchableOpacity, TextInput, Alert, View, FlatList } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import styles from '../styles/EditGoalStyle';
import useAuthFetch from '../utils/useAuthFetch';
import HomePage from './HomePage';
import API_BASE_URL from '../utils/environment_variables';

const EditGoalPage = ({ route, navigation }) => {
    const { goalId } = route.params;
    const [goal, setGoal] = useState(null);
    const [habitTargets, setHabitTargets] = useState({});
    const [selectedPeriodicity, setSelectedPeriodicity] = useState('DAILY');
    const [habitOptions, setHabitOptions] = useState([]);
    const { fetchWithAuth } = useAuthFetch();

    useEffect(() => {
        const fetchGoalData = async () => {
            try {
                const goalData = await fetchWithAuth(`${API_BASE_URL}/goals/${goalId}`);
                if (goalData && !goalData.error) {
                    setGoal(goalData);

                    const habits = goalData.habits ? goalData.habits.split(';') : [];
                    const targets = goalData.target ? goalData.target.split(';') : [];

                    if (habits.length === targets.length) {
                        const habitTargets = habits.reduce((acc, habit, index) => {
                            acc[habit] = targets[index];
                            return acc;
                        }, {});

                        setHabitTargets(habitTargets);
                        setHabitOptions(habits);
                        setSelectedPeriodicity(goalData.periodicity);
                    } else {
                        console.error('Habits and targets length mismatch or missing data');
                    }
                } else {
                    console.error('Error fetching goal data:', goalData.error);
                }
            } catch (error) {
                console.error('Error fetching goal data:', error);
            }
        };
        fetchGoalData();
    }, [goalId]);

    const handleTargetChange = (habit, value) => {
        setHabitTargets(prevTargets => ({ ...prevTargets, [habit]: value }));
    };

    const handleUpdateGoal = async () => {
        if (!selectedPeriodicity || !Object.keys(habitTargets).length) {
            Alert.alert('Error', 'Periodicity and habits are required.');
            return;
        }
    
        const updatedGoal = {
            goalID: goalId,
            user: goal.user ? { userID: goal.user.userID } : null,
            periodicity: selectedPeriodicity,
            target: Object.values(habitTargets).join(';'),
            habits: Object.keys(habitTargets).join(';'),
            progress: goal.progress || '',
        };
    
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/goals/${goalId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedGoal),
            });
    
            if (response.error) {
                throw new Error(response.error);
            }
    
            if (response.status === 200 || response.status === undefined) {
                Alert.alert('Success', 'Goal updated successfully!');
                navigation.navigate(HomePage);
            } else {
                Alert.alert('Error', `Unexpected response status: ${response.status}`);
            }
        } catch (error) {
            console.error('Error updating goal:', error.message);
            Alert.alert('Error', `Failed to update goal: ${error.message}`);
        }
    };    
    
    const handleDeleteGoal = async () => {
        Alert.alert(
            'Confirm Deletion',
            'Are you sure you want to delete this goal?',
            [
                {
                    text: 'Cancel',
                    style: 'cancel',
                },
                {
                    text: 'Delete',
                    onPress: async () => {
                        try {
                            const response = await fetchWithAuth(`${API_BASE_URL}/goals/${goalId}`, {
                                method: 'DELETE',
                            });
                            if (response === null) {
                                Alert.alert('Success', 'Goal deleted successfully!');
                                navigation.navigate('HomePage');
                            } else if (response && response.error) {
                                throw new Error(response.error);
                            } else {
                                Alert.alert('Error', `Unexpected response: ${JSON.stringify(response)}`);
                            }
                        } catch (error) {
                            console.error('Error deleting goal:', error);
                            Alert.alert('Error', `Failed to delete goal: ${error.message}`);
                        }
                    },
                },
            ],
            { cancelable: false }
        );
    };

    const renderHeader = () => (
        <View style={styles.headerContainer}>
            <Text style={styles.title}>Edit Goal</Text>
            <Text style={styles.header}>Select Periodicity:</Text>
            <Picker
                selectedValue={selectedPeriodicity}
                style={styles.picker}
                onValueChange={(itemValue) => setSelectedPeriodicity(itemValue)}
            >
                <Picker.Item label="DAILY" value="DAILY" />
                <Picker.Item label="WEEKLY" value="WEEKLY" />
                <Picker.Item label="MONTHLY" value="MONTHLY" />
                <Picker.Item label="ANNUALY" value="ANNUALY" />
            </Picker>
        </View>
    );

    const renderItem = ({ item }) => (
        <View style={styles.listItem}>
            <Text style={styles.itemText}>{item}</Text>
            <TextInput
                style={styles.input}
                value={habitTargets[item]}
                onChangeText={(value) => handleTargetChange(item, value)}
                keyboardType="numeric"
                placeholder="Enter target"
            />
        </View>
    );

    const renderFooter = () => (
        <View style={styles.footerContainer}>
            <TouchableOpacity style={styles.button} onPress={handleUpdateGoal}>
                <Text style={styles.buttonText}>Update Goal</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.button} onPress={handleDeleteGoal}>
                <Text style={styles.buttonText}>Delete Goal</Text>
            </TouchableOpacity>
        </View>
    );

    if (!goal) {
        return (
            <View style={styles.container}>
                <FlatList
                    data={[]}
                    ListHeaderComponent={<Text style={styles.title}>No Goal Found</Text>}
                    ListEmptyComponent={
                        <View style={styles.emptyContainer}>
                            <Text>The goal you were trying to edit has been deleted or does not exist.</Text>
                            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('MainGoalPage')}>
                                <Text style={styles.buttonText}>Go to Main Goals Page</Text>
                            </TouchableOpacity>
                        </View>
                    }
                />
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <FlatList
                data={habitOptions}
                keyExtractor={(item) => item}
                renderItem={renderItem}
                ListHeaderComponent={renderHeader}
                ListFooterComponent={renderFooter}
                contentContainerStyle={styles.flatListContent}
            />
        </View>
    );
};

export default EditGoalPage;
