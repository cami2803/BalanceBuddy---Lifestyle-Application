import { StyleSheet } from 'react-native';

const LandingStyle = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#e0afa0',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 20,
    },
    title: {
        fontSize: 32,
        fontWeight: 'bold',
        color: '#573d3c',
        marginBottom: 20,
    },
    description: {
        fontSize: 18,
        textAlign: 'center',
        color: '#573d3c',
        marginBottom: 40,
    },
    button: {
        backgroundColor: '#f4f3ee',
        paddingVertical: 15,
        paddingHorizontal: 40,
        borderRadius: 10,
        marginVertical: 10,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 18,
        fontWeight: 'bold',
    },
});

export default LandingStyle;