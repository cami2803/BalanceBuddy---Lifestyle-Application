import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import styles from '../styles/AccountStyle';
import API_BASE_URL from '../utils/environment_variables';

const AccountPage = ({ navigation }) => {
    const [error, setError] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [oldPassword, setOldPassword] = useState('');
    const [existingPassword, setExistingPassword] = useState('');
    const [id, setId] = useState(null);

    useEffect(() => {
        const fetchAccountDetails = async () => {
            try {
                const accessToken = await AsyncStorage.getItem('accessToken');
                const response = await fetch(`${API_BASE_URL}/user/me`, {
                    headers: { 'Authorization': `Bearer ${accessToken}` }
                });
    
                if (response.ok) {
                    const user = await response.json();
                    setId(user.id);
    
                    const userDetailsResponse = await fetch(`${API_BASE_URL}/user/details/${user.id}`, {
                        headers: { 'Authorization': `Bearer ${accessToken}` }
                    });
    
                    if (userDetailsResponse.ok) {
                        const fullUserData = await userDetailsResponse.json();
                        setFirstname(fullUserData.firstname);
                        setLastname(fullUserData.lastname);
                        setEmail(fullUserData.email);
                        setExistingPassword(fullUserData.password);
                    } else {
                        Alert.alert('Error', 'Failed to fetch user details.');
                    }
                } else {
                    Alert.alert('Error', 'Failed to fetch account details.');
                }
            } catch (error) {
                console.error('Error fetching account details:', error);
            }
        };
        fetchAccountDetails();
    }, []);
    

    const saveAccountDetails = async () => {
        if (!id) {
            Alert.alert('Error', 'User ID is missing. Please try again.');
            return;
        }
    
        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
    
            const updateData = {
                firstname,
                lastname,
                email,
            };
    
            const response = await fetch(`${API_BASE_URL}/user/update/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(updateData),
            });
    
            const contentType = response.headers.get('Content-Type');
            let responseData;
    
            if (contentType && contentType.includes('application/json')) {
                responseData = await response.json();
            } else {
                responseData = await response.text();
            }
            if (response.ok) {
                Alert.alert('Success', 'Account details updated successfully.');
            } else {
                Alert.alert('Error', responseData.message || 'Failed to update account details.');
            }
        } catch (error) {
            console.error('Error saving account details:', error);
        }
    };
    

    const changePassword = async () => {
        setError('');
        
        if (oldPassword === newPassword) {
            setError("New password cannot be the same as the old password.");
            return;
        }
    
        try {
            const accessToken = await AsyncStorage.getItem('accessToken');
            const response = await fetch(`${API_BASE_URL}/user/change-password`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify({
                    email: email, 
                    oldPassword: oldPassword,
                    newPassword: newPassword,
                }),
            });
    
            if (response.ok) {
                Alert.alert('Success', 'Password changed successfully.');
                navigation.navigate('LoginPage');
            } else {
                const errorResponse = await response.json();
                Alert.alert('Error', errorResponse.message || 'Failed to change password.');
            }
        } catch (error) {
            console.error('Error changing password:', error);
            setError('An unexpected error occurred.');
        }
    };
    

    const deleteAccount = async () => {
        Alert.alert(
            'Confirm Deletion',
            'Are you sure you want to delete your account?',
            [
                {
                    text: 'Cancel',
                    style: 'cancel',
                },
                {
                    text: 'Delete',
                    onPress: async () => {
                        try {
                            const accessToken = await AsyncStorage.getItem('accessToken');
                            const response = await fetch(`${API_BASE_URL}/user/delete/${email}`, {
                                method: 'DELETE',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': `Bearer ${accessToken}`
                                },
                            });

                            if (response.ok) {
                                await AsyncStorage.removeItem('accessToken');
                                Alert.alert('Account Deleted', 'Your account has been deleted.');
                                navigation.navigate('LoginPage');
                            } else {
                                Alert.alert('Error', 'Failed to delete account.');
                            }
                        } catch (error) {
                            console.error('Error deleting account:', error);
                        }
                    },
                },
            ],
            { cancelable: false }
        );
    };

    return (
        <View style={styles.container}>
            <Text style={styles.title}>Account Settings</Text>

            <View style={styles.labelInputContainer}>
                <Text style={styles.label}>First Name:</Text>
                <TextInput
                    style={styles.input}
                    placeholder="First Name"
                    value={firstname}
                    onChangeText={setFirstname}
                />
            </View>

            <View style={styles.labelInputContainer}>
                <Text style={styles.label}>Last Name:</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Last Name"
                    value={lastname}
                    onChangeText={setLastname}
                />
            </View>

            <View style={styles.labelInputContainer}>
                <Text style={styles.label}>Email:</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Email"
                    value={email}
                    onChangeText={setEmail}
                />
            </View>
            
            <TouchableOpacity style={styles.button} onPress={saveAccountDetails}>
                <Text style={styles.buttonText}>Save</Text>
            </TouchableOpacity>

            <Text style={styles.title}>Change Password</Text>

            <View style={styles.labelInputContainer}>
                <Text style={styles.label}>Old Password:</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Old Password"
                    secureTextEntry
                    value={oldPassword}
                    onChangeText={setOldPassword}
                />
            </View>

            <View style={styles.labelInputContainer}>
                <Text style={styles.label}>New Password:</Text>
                <TextInput
                    style={styles.input}
                    placeholder="New Password"
                    secureTextEntry
                    value={newPassword}
                    onChangeText={setNewPassword}
                />
            </View>

            <TouchableOpacity style={styles.button} onPress={changePassword}>
                <Text style={styles.buttonText}>Change Password</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.button} onPress={deleteAccount}>
                <Text style={styles.buttonText}>Delete Account</Text>
            </TouchableOpacity>
        </View>
    );
};

export default AccountPage;
