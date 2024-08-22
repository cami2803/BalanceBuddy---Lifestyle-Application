import { StyleSheet } from 'react-native';

const MainGoalStyle = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        marginBottom: 20,
        fontWeight: 'bold'
    },
    goalText: {
        fontSize: 18,
        marginTop: 20,
        marginBottom: 40, 
        fontWeight: 'bold'
    },
    buttonContainer: {
        marginTop: 10,
        width: '80%',
    },
    button: {
        backgroundColor: '#f4f3ee',
        padding: 10,
        borderRadius: 5,
        marginBottom: 45,
    },
    buttonText: {
        color: '#406e8e',
        textAlign: 'center',
        fontSize: 16,
        fontWeight: 'bold'
    },
});

export default MainGoalStyle;
