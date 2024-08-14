import React, { useState, useCallback } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, Modal, Button } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';

const RegisterPage = ({ navigation }) => {
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [showConstraints, setShowConstraints] = useState(false);

    useFocusEffect(
        useCallback(() => {
            setFirstname('');
            setLastname('');
            setEmail('');
            setPassword('');
            setConfirmPassword('');
            setError('');
            setShowConstraints(false);
        }, [])
    );

    const handleRegister = async () => {
        setError('');
        if (!firstname || !lastname || !email || !password || !confirmPassword) {
            setError('All fields are required');
            return;
        }
        if (password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        try {
            const response = await fetch('http://10.0.2.2:8080/api/auth/register', { // emulator
                //const response = await fetch('http://192.168.1.130:8080/api/auth/register', { // phone

                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ firstname, lastname, email, password }),
            });

            const data = await response.json();
            if (response.ok) {
                await AsyncStorage.setItem('accessToken', data.accessToken);
                Alert.alert('Success', 'Registered successfully');
                navigation.navigate('HomePage');
            } else {
                if (Array.isArray(data.errors)) {
                    setError(data.errors.join('\n'));
                } else {
                    setError('Registration failed. Please check your input.');
                }
            }
        } catch (error) {
            console.error('Register Error:', error);
            setError('An error occurred while registering');
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Register</Text>
            <TextInput
                style={styles.input}
                placeholder="First Name"
                value={firstname}
                onChangeText={setFirstname}
                placeholderTextColor="#406e8e"
            />
            <TextInput
                style={styles.input}
                placeholder="Last Name"
                value={lastname}
                onChangeText={setLastname}
                placeholderTextColor="#406e8e"
            />
            <TextInput
                style={styles.input}
                placeholder="Email"
                value={email}
                onChangeText={setEmail}
                placeholderTextColor="#406e8e"
            />
            <TextInput
                style={styles.input}
                placeholder="Password"
                value={password}
                onChangeText={setPassword}
                secureTextEntry
                placeholderTextColor="#406e8e"
            />
            <TextInput
                style={styles.input}
                placeholder="Confirm Password"
                value={confirmPassword}
                onChangeText={setConfirmPassword}
                secureTextEntry
                placeholderTextColor="#406e8e"
            />
            {error ? <Text style={styles.errorText}>{error}</Text> : null}
            <TouchableOpacity style={styles.button} onPress={handleRegister}>
                <Text style={styles.buttonText}>Register</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.constraintsButton} onPress={() => setShowConstraints(true)}>
                <Text style={styles.constraintsButtonText}>View Registration Constraints</Text>
            </TouchableOpacity>
            <Text style={styles.signInText} onPress={() => navigation.navigate('LoginPage')}>
                Already have an account? Sign in <Text style={styles.signUpLinkUnderline}>here</Text>
            </Text>

            <Modal
                animationType="slide"
                transparent={true}
                visible={showConstraints}
                onRequestClose={() => setShowConstraints(false)}
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Registration Constraints</Text>
                        <Text style={styles.modalText}>- Email must be in a valid format</Text>
                        <Text style={styles.modalText}>- First Name and Last Name must be between 1 and 30 characters</Text>
                        <Text style={styles.modalText}>- Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character</Text>
                        <Button title="Close" onPress={() => setShowConstraints(false)} />
                    </View>
                </View>
            </Modal>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#573d3c',
        marginBottom: 20,
    },
    input: {
        width: '80%',
        height: 40,
        borderColor: '#406e8e',
        borderWidth: 1,
        borderRadius: 5,
        paddingHorizontal: 10,
        marginBottom: 20,
        backgroundColor: '#f4f3ee',
        color: '#406e8e',
    },
    button: {
        width: '80%',
        backgroundColor: '#f4f3ee',
        padding: 15,
        borderRadius: 5,
        alignItems: 'center',
        marginBottom: 20,
        marginTop: 40,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
    constraintsButton: {
        marginBottom: 20,
        padding: 10,
    },
    constraintsButtonText: {
        color: '#406e8e',
        textDecorationLine: 'underline',
    },
    signInText: {
        color: '#573d3c',
        fontSize: 16,
    },
    errorText: {
        color: 'red',
        fontSize: 16,
        marginBottom: 10,
    },
    signUpLinkUnderline: {
        textDecorationLine: 'underline',
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContent: {
        width: '80%',
        padding: 20,
        backgroundColor: 'white',
        borderRadius: 10,
        alignItems: 'center',
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    modalText: {
        fontSize: 16,
        marginBottom: 10,
    },
});

export default RegisterPage;