import { StyleSheet } from 'react-native';

const LoginStyle = StyleSheet.create({
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
    input: {
        width: '80%',
        height: 40,
        borderColor: '#406e8e',
        borderWidth: 1,
        borderRadius: 5,
        paddingHorizontal: 10,
        marginBottom: 20,
        backgroundColor: '#f4f3ee',
        color: '#406e8e',
    },
    forgotPasswordButton: {
        alignSelf: 'flex-end',
        marginBottom: 20,
        paddingHorizontal: 45,
    },
    forgotPasswordText: {
        color: '#406e8e',
    },
    button: {
        width: '80%',
        backgroundColor: '#f4f3ee',
        padding: 15,
        borderRadius: 5,
        alignItems: 'center',
        marginBottom: 20,
        marginTop: 40,
    },
    buttonText: {
        color: '#406e8e',
        fontSize: 16,
        fontWeight: 'bold',
    },
    signUpText: {
        color: '#573d3c',
        fontSize: 16,
    },
    signUpLink: {
        color: '#573d3c',
        fontSize: 16,
    },
    signUpLinkUnderline: {
        textDecorationLine: 'underline',
    },
    errorText: {
        color: 'red',
        fontSize: 16,
        marginBottom: 10,
    },
});

export default LoginStyle;