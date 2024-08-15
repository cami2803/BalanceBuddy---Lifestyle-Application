import React, { useState, useCallback } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert, Modal, Button } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';
import styles from '../styles/RegisterStyle';

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

export default RegisterPage;