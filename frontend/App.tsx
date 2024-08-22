import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import LandingPage from './src/screens/LandingPage';
import LoginPage from './src/screens/LoginPage';
import RegisterPage from './src/screens/RegisterPage';
import HomePage from './src/screens/HomePage';
import MainGoalPage from './src/screens/MainGoalPage';
import CurrentGoalPage from './src/screens/CurrentGoalPage';
import EditGoalPage from './src/screens/EditGoalPage';
import NewGoalPage from './src/screens/NewGoalPage';
import AddProgressPage from './src/screens/AddProgressPage';

type RootStackParamList = {
    LandingPage: undefined;
    LoginPage: undefined;
    RegisterPage: undefined;
    HomePage: undefined;
    MainGoalPage: undefined;
    CurrentGoalPage: undefined;
    EditGoalPage: undefined;
    NewGoalPage: undefined;
    AddProgressPage: undefined;
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
                <Stack.Screen name="MainGoalPage" component={MainGoalPage} options={{ title: 'Main Goal Page' }} />
                <Stack.Screen name="CurrentGoalPage" component={CurrentGoalPage} options={{ title: 'Current Goal Page' }}/>
                <Stack.Screen name="EditGoalPage" component={EditGoalPage} options={{ title: 'Edit Goal Page' }}/>
                <Stack.Screen name="NewGoalPage" component={NewGoalPage} options={{ title: 'New Goal Page' }}/>
                <Stack.Screen name="AddProgressPage" component={AddProgressPage} options={{ title: 'Add Progress Page' }} />
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default App;
