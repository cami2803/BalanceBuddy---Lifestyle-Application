import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import styles from '../styles/MainGoalStyle';
import CircularProgress from '../components/CircularProgress';
import useAuthFetch from '../utils/useAuthFetch';
import API_BASE_URL from '../utils/environment_variables';

const MainGoalPage = ({ navigation }) => {
    const [goal, setGoal] = useState(null);
    const { fetchWithAuth, loading } = useAuthFetch();

    const fetchCurrentUserAndGoal = async () => {
        try {
            const userResponse = await fetchWithAuth(`${API_BASE_URL}/user/me`);
            if (!userResponse || !userResponse.id) {
                console.error('No user ID found.');
                setGoal(null);
                return;
            }

            const goalResponse = await fetchWithAuth(`${API_BASE_URL}/user/getGoals/${userResponse.id}`);
            if (goalResponse && Array.isArray(goalResponse) && goalResponse.length > 0) {
                setGoal(goalResponse[0]);
            } else {
                setGoal(null);
            }
        } catch (error) {
            console.error('Error fetching user or goal:', error);
            setGoal(null);
        }
    };

    useEffect(() => {
        fetchCurrentUserAndGoal();
    }, [fetchWithAuth]);

    if (loading) {
        return <Text>Loading...</Text>;
    }

    const computeGoalProgress = (goal) => {
        const progressArray = goal.progress.split(';');
        const targetArray = goal.target.split(';');
    
        let totalTarget = 0;
        let totalProgress = 0;
    
        for (let i = 0; i < progressArray.length; i++) {
            totalTarget += parseInt(targetArray[i], 10);
            totalProgress += parseInt(progressArray[i], 10);
        }
    
        if (totalTarget === 0) 
            return 0;
    
        let progressPercentage = (totalProgress * 100) / totalTarget;
    
        if (progressPercentage >= 100) 
            return 100;
    
        return parseFloat(progressPercentage.toFixed(2));
    };

    const progress = goal ? computeGoalProgress(goal) : 0;

    return (
        <View style={styles.container}>
            <Text style={styles.title}>My Goal</Text>
            {goal ? (
                <>
                    <Text style={styles.goalText}>Current Goal Progress: </Text>
                        <CircularProgress percentage={progress} />
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('CurrentGoalPage', { goalId: goal.goalID })}>
                            <Text style={styles.buttonText}>View Current Goal</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('NewGoalPage')}>
                            <Text style={styles.buttonText}>Create New Goal</Text>
                        </TouchableOpacity>
                    </View>
                </>
            ) : (
                <>
                    <Text style={styles.goalText}>No current goal</Text>
                    <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('NewGoalPage')}>
                        <Text style={styles.buttonText}>Create New Goal</Text>
                    </TouchableOpacity>
                </>
            )}
        </View>
    );
};

export default MainGoalPage;
