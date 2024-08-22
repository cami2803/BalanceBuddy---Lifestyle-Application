import { StyleSheet } from 'react-native';

const CurrentGoalStyle = StyleSheet.create({
    container: {
        justifyContent: 'center',
        flex: 1,
        padding: 20,
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
        textAlign: 'center',
    },
    header: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    item: {
        fontSize: 16,
        marginBottom: 5,
    },
    habitContainer: {
        marginBottom: 20,
    },
    habitName: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 5,
    },
    button: {
        backgroundColor: '#f4f3ee',
        padding: 10,
        borderRadius: 5,
        marginBottom: 20,
    },
    buttonText: {
        color: '#406e8e',
        textAlign: 'center',
        fontSize: 16,
        fontWeight: 'bold',
    },
    scrollView: {
        flexGrow: 1,
    },
    periodicity: {
        marginBottom: 30,
    },
});

export default CurrentGoalStyle;