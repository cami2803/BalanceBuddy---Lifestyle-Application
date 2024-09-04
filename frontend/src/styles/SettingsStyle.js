import { StyleSheet } from 'react-native';

const SettingsStyle = StyleSheet.create({
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
    settingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: 16,
        paddingHorizontal: 10,
        backgroundColor: '#f4f3ee', 
        borderRadius: 5, 
        paddingVertical: 10, 
    },
    settingText: {
        fontSize: 18,
        color: '#573d3c',
    },
    button: {
        width: '80%',
        backgroundColor: '#f4f3ee',
        padding: 15,
        alignItems: 'center',
        marginBottom: 20,
        marginTop: 30,
        borderRadius: 10,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 3,
        elevation: 3,
        alignSelf: 'center',
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default SettingsStyle;
