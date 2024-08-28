import React, { useEffect, useState } from 'react';
import { Text, TouchableOpacity, TextInput, Alert, View, FlatList, Modal } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import styles from '../styles/EditGoalStyle';
import useAuthFetch from '../utils/useAuthFetch';
import API_BASE_URL from '../utils/environment_variables';

const EditGoalPage = ({ route, navigation }) => {
    const { goalId } = route.params;
    const [goal, setGoal] = useState(null);
    const [habitTargets, setHabitTargets] = useState({});
    const [selectedPeriodicity, setSelectedPeriodicity] = useState('DAILY');
    const [habitOptions, setHabitOptions] = useState([]);
    const [availableHabits, setAvailableHabits] = useState([]);
    const [showAddHabitModal, setShowAddHabitModal] = useState(false);
    const [selectedHabit, setSelectedHabit] = useState('');
    const [newHabitTarget, setNewHabitTarget] = useState('');
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

    useEffect(() => {
        const fetchAvailableHabits = async () => {
            try {
                const habitsData = await fetchWithAuth(`${API_BASE_URL}/habits`);
                if (habitsData && !habitsData.error) {
                    setAvailableHabits(habitsData.map(habit => habit.name)); 
                } else {
                    console.error('Error fetching available habits:', habitsData.error);
                }
            } catch (error) {
                console.error('Error fetching available habits:', error);
            }
        };
        fetchAvailableHabits();
    }, []);
    

    const handleTargetChange = (habit, value) => {
        setHabitTargets(prevTargets => ({ ...prevTargets, [habit]: value }));
    };

    const handleAddHabit = async () => {
        if (!selectedHabit || !newHabitTarget) {
            Alert.alert('Error', 'Habit and target value are required.');
            return;
        }

        const updatedHabitTargets = { ...habitTargets, [selectedHabit]: newHabitTarget };
        const updatedHabitOptions = [...habitOptions, selectedHabit];
        const updatedProgress = goal.progress ? goal.progress.split(';') : [];
        updatedProgress.push('0');
    
        setHabitTargets(updatedHabitTargets);
        setHabitOptions(updatedHabitOptions);
        setShowAddHabitModal(false);
    
        try {
            const updatedGoal = {
                goalID: goalId,
                user: goal.user ? { userID: goal.user.userID } : null,
                periodicity: selectedPeriodicity,
                target: Object.values(updatedHabitTargets).join(';'),
                habits: Object.keys(updatedHabitTargets).join(';'),
                progress: updatedProgress.join(';'),
            };
    
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
            } else {
                Alert.alert('Error', `Unexpected response status: ${response.status}`);
            }
        } catch (error) {
            console.error('Error updating goal:', error.message);
            Alert.alert('Error', `Failed to update goal: ${error.message}`);
        }
    };

    const handleDeleteHabit = async (habitToDelete) => {
        const updatedHabitTargets = { ...habitTargets };
        const updatedHabitOptions = habitOptions.filter(habit => habit !== habitToDelete);
        const updatedProgress = goal.progress ? goal.progress.split(';') : [];
        const habitIndex = Object.keys(habitTargets).indexOf(habitToDelete);
        
        delete updatedHabitTargets[habitToDelete];
        if (habitIndex > -1) {
            updatedProgress.splice(habitIndex, 1); 
        }
    
        try {
            const updatedGoal = {
                goalID: goalId,
                user: goal.user ? { userID: goal.user.userID } : null,
                periodicity: selectedPeriodicity,
                target: Object.values(updatedHabitTargets).join(';'),
                habits: updatedHabitOptions.join(';'),
                progress: updatedProgress.join(';'),
            };
    
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
                Alert.alert('Success', 'Habit deleted successfully!');
                setHabitTargets(updatedHabitTargets);
                setHabitOptions(updatedHabitOptions);
            } else {
                Alert.alert('Error', `Unexpected response status: ${response.status}`);
            }
        } catch (error) {
            console.error('Error updating goal:', error.message);
            Alert.alert('Error', `Failed to delete habit: ${error.message}`);
        }
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
                navigation.navigate('HomePage');
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
            <TouchableOpacity
                style={styles.deleteButton}
                onPress={() => handleDeleteHabit(item)}
            >
                <Text style={styles.deleteButtonText}>Delete</Text>
            </TouchableOpacity>
        </View>
    );
      

    const renderFooter = () => (
        <View style={styles.footerContainer}>
            <TouchableOpacity style={styles.button} onPress={() => setShowAddHabitModal(true)}>
                <Text style={styles.buttonText}>Add More Habits</Text>
            </TouchableOpacity>
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

            <Modal
                transparent={true}
                visible={showAddHabitModal}
                onRequestClose={() => setShowAddHabitModal(false)}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Add New Habit</Text>
                        <Picker
                            selectedValue={selectedHabit}
                            style={styles.picker}
                            onValueChange={(itemValue) => setSelectedHabit(itemValue)}
                        >
                            {availableHabits.map((habit, index) => (
                                <Picker.Item key={index} label={habit} value={habit} />
                            ))}
                        </Picker>

                        <TextInput
                            style={styles.input}
                            value={newHabitTarget}
                            onChangeText={setNewHabitTarget}
                            keyboardType="numeric"
                            placeholder="Enter target"
                        />
                        <TouchableOpacity style={styles.button} onPress={handleAddHabit}>
                            <Text style={styles.buttonText}>Add Habit</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.button} onPress={() => setShowAddHabitModal(false)}>
                            <Text style={styles.buttonText}>Cancel</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
        </View>
    );
};

export default EditGoalPage;
