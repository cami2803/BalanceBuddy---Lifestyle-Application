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
import HabitsPage from './src/screens/HabitsPage';
import CreateHabitPage from './src/screens/CreateHabitPage';
import EditHabitsPage from './src/screens/EditHabitsPage';
import ReportPage from './src/screens/ReportPage';
import SettingsPage from './src/screens/SettingsPage';
import AccountPage from './src/screens/AccountPage';

const Stack = createStackNavigator();

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
                <Stack.Screen name="HabitsPage" component={HabitsPage} options={{ title: 'Habits Page' }} />
                <Stack.Screen name="CreateHabitPage" component={CreateHabitPage} options={{ title: 'Create Habit Page' }} />
                <Stack.Screen name="EditHabitsPage" component={EditHabitsPage} options={{ title: 'Edit Habits Page' }} />
                <Stack.Screen name="ReportPage" component={ReportPage} options={{ title: 'Report Page' }}/>
                <Stack.Screen name="SettingsPage" component={SettingsPage} options={{ title: 'Settings Page' }}/>
                <Stack.Screen name="AccountPage" component={AccountPage} options={{ title: 'Account Page' }}/>
            </Stack.Navigator>
        </NavigationContainer>
    );
};

export default App;
