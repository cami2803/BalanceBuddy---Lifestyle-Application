import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, TouchableOpacity } from 'react-native';
import Calendar from '../components/Calendar';
import styles from '../styles/HistoryStyle';

const HistoryPage = ({ navigation }) => {
    const [selectedDate, setSelectedDate] = useState(null);
    const [dailyProgress, setDailyProgress] = useState([]);
    const [dailyProgressPercentage, setDailyProgressPercentage] = useState('0%');

    useEffect(() => {
        if (selectedDate) {
            const fetchProgress = async () => {
                try {
                    const mockData = [
                        { habit: 'Exercise', progress: '30', unit: 'minutes' },
                        { habit: 'Reading', progress: '1', unit: 'hour' }
                    ];
                    setDailyProgress(mockData);
                    setDailyProgressPercentage('70%');
                } catch (error) {
                    console.error('Error fetching progress:', error);
                }
            };

            fetchProgress();
        }
    }, [selectedDate]);

    const renderItem = ({ item }) => (
        <View style={styles.progressItem}>
            <Text style={styles.progressText}>
                {item.habit}: {item.progress} {item.unit}
            </Text>
        </View>
    );

    const markedDates = {
        '2024-09-01': { color: '#FF9999' }, // Example for daily goal
        '2024-09-02': { color: '#99FF99' }, // Example for weekly goal
        '2024-09-10': { color: '#9999FF' }  // Example for monthly goal
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>History</Text>
            <Calendar
                onDayPress={(day) => setSelectedDate(day)}
                markedDates={markedDates}
                selectedDate={selectedDate}
            />
            <Text style={styles.progressText}>Progress: {dailyProgressPercentage}</Text>
            <FlatList
                data={dailyProgress}
                renderItem={renderItem}
                keyExtractor={(item, index) => index.toString()}
                contentContainerStyle={styles.progressContainer}
                ListEmptyComponent={<Text style={styles.progressText}>No progress data for selected date.</Text>}
            />
            <View style={styles.colorLegendContainer}>
                <Text style={styles.legendTitle}>Color Legend</Text>
                <View style={styles.legendItem}>
                    <View style={[styles.legendColorBox, { backgroundColor: '#FF9999' }]} />
                    <Text style={styles.legendText}>Daily Goal</Text>
                </View>
                <View style={styles.legendItem}>
                    <View style={[styles.legendColorBox, { backgroundColor: '#99FF99' }]} />
                    <Text style={styles.legendText}>Weekly Goal</Text>
                </View>
                <View style={styles.legendItem}>
                    <View style={[styles.legendColorBox, { backgroundColor: '#9999FF' }]} />
                    <Text style={styles.legendText}>Monthly Goal</Text>
                </View>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={styles.button}
                    onPress={() => navigation.navigate('HomePage')}
                >
                    <Text style={styles.buttonText}>Back to Home</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

export default HistoryPage;