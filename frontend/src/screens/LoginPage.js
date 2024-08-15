import React, { useState, useCallback } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';
import styles from '../styles/LoginStyle';

const LoginPage = ({ navigation }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    useFocusEffect(
        useCallback(() => {
            setEmail('');
            setPassword('');
            setError('');
        }, [])
    );

    const handleLogin = async () => {
        setError(''); 
        if (!email || !password) {
            setError('Both email and password fields are required');
            return;
        }

        try {
            const response = await fetch('http://10.0.2.2:8080/api/auth/login', { // emulator
            //const response = await fetch('http://192.168.1.130:8080/api/auth/login', { // phone
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            const data = await response.json();
            if (response.ok) {
                await AsyncStorage.setItem('accessToken', data.accessToken);
                Alert.alert('Success', 'Logged in successfully');
                navigation.navigate('HomePage');
            } else {
                setError(data.error || 'Incorrect credentials!');
            }
        } catch (error) {
            setError('An error occurred while logging in');
        }
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Login</Text>
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
            {error ? <Text style={styles.errorText}>{error}</Text> : null}
            <TouchableOpacity style={styles.forgotPasswordButton}>
                <Text style={styles.forgotPasswordText}>Forgot my password</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.button} onPress={handleLogin}>
                <Text style={styles.buttonText}>Login</Text>
            </TouchableOpacity>
            <Text style={styles.signUpText}>
                Don't have an account? 
                <Text 
                    style={styles.signUpLink} 
                    onPress={() => navigation.navigate('RegisterPage')}
                >
                    {' '}Sign up <Text style={styles.signUpLinkUnderline}>here</Text>
                </Text>
            </Text>
        </View>
    );
};

export default LoginPage;
