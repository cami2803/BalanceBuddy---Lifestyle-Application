import React from 'react';
import { View, Text, Button, StyleSheet, TouchableOpacity } from 'react-native';

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

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#e0afa0',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    title: {
        fontSize: 32,
        fontWeight: 'bold',
        color: '#573d3c',
        marginBottom: 20,
    },
    description: {
        fontSize: 18,
        textAlign: 'center',
        color: '#573d3c',
        marginBottom: 40,
    },
    button: {
        backgroundColor: '#f4f3ee',
        paddingVertical: 15,
        paddingHorizontal: 40,
        borderRadius: 10,
        marginVertical: 10,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 18,
        fontWeight: 'bold',
    },
});

export default LandingPage;
