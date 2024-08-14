import React, { useState, useCallback } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';

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
    forgotPasswordButton: {
        alignSelf: 'flex-end',
        marginBottom: 20,
        paddingHorizontal: 45,
    },
    forgotPasswordText: {
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
    signUpText: {
        color: '#573d3c',
        fontSize: 16,
    },
    signUpLink: {
        color: '#573d3c',
        fontSize: 16,
    },
    signUpLinkUnderline: {
        textDecorationLine: 'underline',
    },
    errorText: {
        color: 'red',
        fontSize: 16,
        marginBottom: 10,
    },
});

export default LoginPage;
