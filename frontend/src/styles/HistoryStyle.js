import { StyleSheet, Dimensions  } from 'react-native';

const { height } = Dimensions.get('window');

const HistoryStyle = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
        justifyContent: 'flex-start',
        alignItems: 'center',
        backgroundColor: '#e0afa0',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 16,
    },
    progressContainer: {
        marginVertical: 16,
        maxHeight: height * 0.6, 
        width: '100%',
        marginVertical: 16,
    },
    progressItem: {
        padding: 15,
        borderWidth: 1,
        borderColor: '#ddd',
        borderRadius: 8,
        marginBottom: 10,
        backgroundColor: '#fff',
    },
    progressText: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    buttonContainer: {
        marginTop: 20,
        alignItems: 'center',
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
    colorLegendContainer: {
        marginTop: 30,
        padding: 10,
        borderTopWidth: 1,
        borderTopColor: '#ddd',
        width: '100%',
    },
    legendTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    legendItem: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 10,
        fontWeight: 'bold',
    },
    legendColorBox: {
        width: 20,
        height: 20,
        marginRight: 10,
        borderRadius: 4,
    },
    legendText: {
        fontSize: 16,
        fontWeight: 'bold',
    },
});

export default HistoryStyle;
