import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import LandingPage from './src/screens/LandingPage';
import LoginPage from './src/screens/LoginPage';
import RegisterPage from './src/screens/RegisterPage';
import HomePage from './src/screens/HomePage';

type RootStackParamList = {
    LandingPage: undefined;
    LoginPage: undefined;
    RegisterPage: undefined;
    HomePage: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

const App = () => {
    return (
        <NavigationContainer>
            <Stack.Navigator initialRouteName="LandingPage">
                <Stack.Screen name="LandingPage" component={LandingPage} options={{ title: 'Welcome' }} />
                <Stack.Screen name="LoginPage" component={LoginPage} options={{ title: 'Login' }} />
                <Stack.Screen name="RegisterPage" component={RegisterPage} options={{ title: 'Register' }} />
                <Stack.Screen name="HomePage" component={HomePage} options={{ title: 'Home Page' }} />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default App;
