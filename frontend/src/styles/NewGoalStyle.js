import { StyleSheet } from 'react-native';

const NewGoalStyle = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#573d3c',
        marginBottom: 16,
        textAlign: 'center',
    },
    section: {
        marginBottom: 16,
    },
    subtitle: {
        fontSize: 18,
        color: '#573d3c',
        marginBottom: 8,
    },
    habitContainer: {
        padding: 8,
        backgroundColor: '#f4f3ee',
        marginBottom: 4,
        borderRadius: 5,
    },
    highlightedHabit: {
        backgroundColor: '#d0f0c0',
    },
    habit: {
        fontSize: 16,
        color: '#406e8e', 
    },
    input: {
        height: 40,
        borderColor: '#406e8e',
        borderWidth: 1,
        borderRadius: 5,
        paddingHorizontal: 10,
        backgroundColor: '#f4f3ee',
        color: '#406e8e', 
    },
    button: {
        width: '80%',
        backgroundColor: '#f4f3ee', 
        padding: 15,
        borderRadius: 5,
        alignItems: 'center',
        marginBottom: 20,
        marginTop: 30,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default NewGoalStyle;
