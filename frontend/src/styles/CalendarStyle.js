import { StyleSheet } from 'react-native';

const CalendarStyle = StyleSheet.create({
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 10,
        backgroundColor: '#fff',
    },
    headerButton: {
        fontSize: 24,
    },
    headerTitle: {
        fontSize: 18,
        fontWeight: 'bold',
    },
    grid: {
        flexDirection: 'row',
        flexWrap: 'wrap',
    },
    cellContainer: {
        width: '14.28%',
        height: 50,
        justifyContent: 'center',
        alignItems: 'center',
        position: 'relative',
    },
    cell: {
        width: '100%',
        height: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: '#ddd',
        backgroundColor: '#fff',
    },
    cellText: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    selectedCell: {
        backgroundColor: '#f4f3ee',
    },
    colorBar: {
        width: '100%',
        height: 4,
        position: 'absolute',
        bottom: 0,
        left: 0,
    },
});

export default CalendarStyle;
