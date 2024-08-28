import { StyleSheet } from 'react-native';

const HabitStyle = StyleSheet.create({
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
    buttonContainer: {
        width: '80%',
        marginTop: 20,
    },
    button: {
        backgroundColor: '#f4f3ee',
        padding: 15,
        borderRadius: 10, 
        alignItems: 'center',
        marginBottom: 10,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 3,
        elevation: 3,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
    habitItem: {
        backgroundColor: '#f4f3ee',
        padding: 15,
        borderRadius: 10,
        alignItems: 'flex-start', 
        marginBottom: 10,
        width: '90%', 
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 3,
        elevation: 3,
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    habitText: {
        color: '#573d3c',
        fontSize: 16,
        fontWeight: 'bold',
    },
    flatListContent: {
        paddingBottom: 30,
        paddingHorizontal: 10,
    },
});

export default HabitStyle;
