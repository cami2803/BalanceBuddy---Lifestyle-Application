import { StyleSheet } from 'react-native';

const EditGoalStyle = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 50, 
    },
    header: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 8, 
    },
    picker: {
        height: 50,
        width: '100%',
        marginBottom: 16, 
        backgroundColor: '#d9937e',
    },
    input: {
        height: 40,
        borderColor: '#f4f3ee', 
        backgroundColor: '#f4f3ee', 
        borderWidth: 1,
        borderRadius: 4,
        paddingHorizontal: 8,
        marginBottom: 16,
        fontWeight: 'bold',
    },
    button: {
        backgroundColor: '#f4f3ee',
        padding: 16,
        borderRadius: 4,
        alignItems: 'center', 
        marginTop: 20,
        marginBottom: 40,
    },
    buttonText: {
        color: '#406e8e',
        textAlign: 'center',
        fontSize: 16,
        fontWeight: 'bold',
    },
    flatListContent: {
        flexGrow: 1,
    },
});

export default EditGoalStyle;
