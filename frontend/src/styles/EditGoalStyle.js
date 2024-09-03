import { StyleSheet } from 'react-native';

const EditGoalStyle = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#e0afa0',
        padding: 16,
    },
    headerContainer: {
        marginBottom: 16,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    header: {
        fontSize: 18,
        marginVertical: 8,
        fontWeight: 'bold',
    },
    picker: {
        height: 50,
        width: '100%',
    },
    listItem: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginVertical: 8,
        padding: 10,
        borderWidth: 1,
        borderColor: '#080808',
        borderRadius: 5,
    },
    itemText: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    input: {
        borderBottomWidth: 1,
        borderColor: '#080808',
        width: '50%',
        padding: 5,
        fontSize: 16,
    },
    footerContainer: {
        marginTop: 16,
    },
    button: {
        backgroundColor: '#f4f3ee',
        padding: 10,
        marginVertical: 5,
        alignItems: 'center',
        borderRadius: 10, 
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
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContent: {
        backgroundColor: '#e0afa5',
        padding: 20,
        borderRadius: 10,
        width: '80%',
    },
    modalTitle: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 15,
    },
    flatListContent: {
        paddingBottom: 20,
    },
    deleteButton: {
        backgroundColor: 'red',
        padding: 5,
        marginLeft: 10,
        borderRadius: 5,
    },
    deleteButtonText: {
        color: 'white',
        fontWeight: 'bold',
    },
});

export default EditGoalStyle;
