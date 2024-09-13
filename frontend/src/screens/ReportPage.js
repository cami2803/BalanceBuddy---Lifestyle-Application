import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import styles from '../styles/ReportStyle';
import CircularProgress from '../components/CircularProgress';
import useAuthFetch from '../utils/useAuthFetch';
import API_BASE_URL from '../utils/environment_variables';
import AsyncStorage from '@react-native-async-storage/async-storage';

const ReportPage = ({ navigation }) => {
    const [habitDetails, setHabitDetails] = useState([]);
    const [percentCompleted, setPercentCompleted] = useState(0);
    const [loadingReport, setLoadingReport] = useState(true);
    const { fetchWithAuth, loading } = useAuthFetch();

    const fetchUserReport = async () => {
        try {
            const userData = await fetchWithAuth(`${API_BASE_URL}/user/me`);
            const userId = userData?.id;

            if (userId) {
                const goals = await fetchWithAuth(`${API_BASE_URL}/user/getGoals/${userId}`);

                if (goals && goals.length > 0) {
                    const goal = goals[0];
                    const habits = goal.habits ? goal.habits.split(';') : [];
                    const targets = goal.target ? goal.target.split(';') : [];
                    const progresses = goal.progress ? goal.progress.split(';') : [];

                    const habitsData = await fetchWithAuth(`${API_BASE_URL}/habits`);
                    const habitMap = habitsData.reduce((map, habit) => {
                        map[habit.name] = habit.unit;
                        return map;
                    }, {});

                    const habitDetails = habits.map((habitName, index) => ({
                        habit: habitName,
                        target: parseFloat(targets[index] || 0),
                        progress: parseFloat(progresses[index] || 0),
                        unit: habitMap[habitName] || '',
                    }));

                    setHabitDetails(habitDetails);

                    const totalTarget = habitDetails.reduce((sum, detail) => sum + detail.target, 0);
                    const totalProgress = habitDetails.reduce((sum, detail) => sum + detail.progress, 0);

                    const percentCompleted = totalTarget > 0 ? (totalProgress / totalTarget) * 100 : 0;
                    setPercentCompleted(percentCompleted);

                    await AsyncStorage.setItem('latestReport', JSON.stringify({
                        habitDetails,
                        percentCompleted,
                        date: new Date().toISOString(),
                    }));
                } else {
                    console.log('No goals found for this user.');
                }
            }
        } catch (error) {
            console.error('Error fetching user report:', error);
        } finally {
            setLoadingReport(false);
        }
    };

    useEffect(() => {
        fetchUserReport();
    }, []);

    if (loading || loadingReport) {
        return <Text>Loading...</Text>;
    }

    if (!habitDetails.length) {
        return <Text>No report data available.</Text>;
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Congratulations!</Text>
            <Text style={styles.subtitle}>Today you managed to:</Text>

            {habitDetails.length > 0 ? (
                habitDetails.map((detail, index) => (
                    <Text key={index} style={styles.habitText}>
                        {detail.habit}: {detail.progress} {detail.unit}
                    </Text>
                ))
            ) : (
                <Text style={styles.habitText}>No habits recorded for today.</Text>
            )}

            <CircularProgress percentage={percentCompleted} />

            <Text style={styles.summaryText}>
                You completed {percentCompleted.toFixed(2)}% of your goal today!
            </Text>

            {percentCompleted >= 10 ? (
                <Text style={styles.keepGoingText}>Keep it going!</Text>
            ) : (
                <Text style={styles.keepGoingText}>Don't worry! Tomorrow you'll do better!</Text>
            )}

            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('HomePage')}
                >
                    <Text style={styles.buttonText}>Close Report</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

export default ReportPage;
