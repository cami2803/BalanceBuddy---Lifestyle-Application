import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import LandingPage from '../screens/LandingPage';
import LoginPage from '../screens/LoginPage';
import RegisterPage from '../screens/RegisterPage';
import HomePage from '../screens/HomePage';
import MainGoalPage from '../screens/MainGoalPage';
import CurrentGoalPage from '../src/screens/CurrentGoalPage';
import EditGoalPage from '../src/screens/EditGoalPage';
import NewGoalPage from '../src/screens/NewGoalPage';
import AddProgressPage from '../screens/AddProgressPage';
import HabitsPage from '../screens/HabitsPage';
import CreateHabitPage from '../screens/CreateHabitPage';
import EditHabitsPage from '../screens/EditHabitsPage';

const Stack = createStackNavigator();

const StackNavigator = () => {
    return (
        <Stack.Navigator initialRouteName="LandingPage">
            <Stack.Screen name="LandingPage" component={LandingPage} />
            <Stack.Screen name="LoginPage" component={LoginPage} />
            <Stack.Screen name="RegisterPage" component={RegisterPage} />
            <Stack.Screen name="HomePage" component={HomePage} />
            <Stack.Screen name="MainGoalPage" component={MainGoalPage} />
            <Stack.Screen name="CurrentGoalPage" component={CurrentGoalPage} />
            <Stack.Screen name="EditGoalPage" component={EditGoalPage} />
            <Stack.Screen name="NewGoalPage" component={NewGoalPage} />
            <Stack.Screen name="AddProgressPage" component={AddProgressPage} />
            <Stack.Screen name="HabitsPage" component={HabitsPage} />
            <Stack.Screen name="CreateHabitPage" component={CreateHabitPage} />
            <Stack.Screen name="EditHabitsPage" component={EditHabitsPage} />
        </Stack.Navigator>
    );
};

export default StackNavigator;
