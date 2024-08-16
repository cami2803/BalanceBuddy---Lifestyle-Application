import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import styles from '../styles/LandingStyle';

const LandingPage = ({ navigation }) => {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>BalanceBuddy</Text>
            <Text style={styles.description}>
                Welcome to BalanceBuddy!
                {"\n"}An application designed to help you improve your lifestyle by managing your daily habits.
            </Text>
            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('LoginPage')}>
                <Text style={styles.buttonText}>Log In</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('RegisterPage')}>
                <Text style={styles.buttonText}>Register</Text>
            </TouchableOpacity>
        </View>
    );
};

export default LandingPage;
