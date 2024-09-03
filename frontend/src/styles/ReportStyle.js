import { StyleSheet } from 'react-native';

const ReportStyle = StyleSheet.create({
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
        marginBottom: 50,
    },
    subtitle: {
        fontSize: 18,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 20,
    },
    buttonContainer: {
        width: '80%',
        marginTop: 20,
    },
    button: {
        backgroundColor: '#f4f3ee',  
        padding: 15,
        alignItems: 'center',
        marginBottom: 10,
        borderRadius: 10,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 4,
        elevation: 5, 
    },
    buttonText: {
        color: '#406e8e',  
        fontSize: 16,
        fontWeight: 'bold',
    },
    progressContainer: {
        alignItems: 'center',
        marginVertical: 20,
    },
    habitText: {
        fontSize: 16,
        marginVertical: 4,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    summaryText: {
        fontSize: 18,
        fontWeight: 'bold',
        textAlign: 'center',
        marginTop: 20,
        marginBottom: 20,
    },
    keepGoingText: {
        fontSize: 16,
        textAlign: 'center',
        marginVertical: 10,
    },
});


export default ReportStyle;
