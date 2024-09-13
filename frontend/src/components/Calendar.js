import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, Dimensions } from 'react-native';
import styles from '../styles/CalendarStyle';

const { width } = Dimensions.get('window');
const cellSize = width / 7;

const Calendar = ({ onDayPress, markedDates, selectedDate }) => {
    const [currentMonth, setCurrentMonth] = useState(new Date());

    const getDaysInMonth = (date) => {
        const year = date.getFullYear();
        const month = date.getMonth();
        const start = new Date(year, month, 1);
        const end = new Date(year, month + 1, 0);
        const days = [];

        for (let i = start.getDay(); i > 0; i--) {
            days.push(null);
        }

        for (let i = 1; i <= end.getDate(); i++) {
            days.push(new Date(year, month, i));
        }

        return days;
    };

    const days = getDaysInMonth(currentMonth);
    const monthName = currentMonth.toLocaleDateString('default', { month: 'long', year: 'numeric' });

    const handlePreviousMonth = () => {
        setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
    };

    const handleNextMonth = () => {
        setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
    };

    const formatDate = (date) => {
        if (!date) return '';
        const day = date.getDate();
        const month = date.getMonth() + 1; // +1 because months start from 0 in this case
        const year = date.getFullYear();
        return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    };

    return (
        <View>
            <View style={styles.header}>
                <TouchableOpacity onPress={handlePreviousMonth}>
                    <Text style={styles.headerButton}>{'<'}</Text>
                </TouchableOpacity>
                <Text style={styles.headerTitle}>{monthName}</Text>
                <TouchableOpacity onPress={handleNextMonth}>
                    <Text style={styles.headerButton}>{'>'}</Text>
                </TouchableOpacity>
            </View>
            <View style={styles.grid}>
                {days.map((day, index) => (
                    <View key={index} style={styles.cellContainer}>
                        <TouchableOpacity
                            style={[
                                styles.cell,
                                day && day.toDateString() === new Date(selectedDate).toDateString() && styles.selectedCell
                            ]}
                            onPress={() => day && onDayPress(formatDate(day))}
                        >
                            <Text style={styles.cellText}>{day ? day.getDate() : ''}</Text>
                        </TouchableOpacity>
                        <View style={[
                            styles.colorBar,
                            { backgroundColor: markedDates[formatDate(day)] ? markedDates[formatDate(day)].color : 'transparent' }
                        ]} />
                    </View>
                ))}
            </View>
        </View>
    );
};

export default Calendar;
