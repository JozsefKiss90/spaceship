import {createContext, useContext, useReducer} from 'react';

const MessageContext = createContext(null);
const MessageDispatchContext = createContext(null);

export function useMessageContext() {
    return useContext(MessageContext);
}

export function useMessageDispatchContext() {
    return useContext(MessageDispatchContext);
}

export function StationContext({children}) {
    const [message, dispatch] = useReducer(
        messageReducer,
        null,
        (e) => <div>{e}</div>
    );
    return (<>
        <MessageContext.Provider value={message}>
            <MessageDispatchContext.Provider value={dispatch}>
                {children}
            </MessageDispatchContext.Provider>
        </MessageContext.Provider>
    </>);
}

const messageReducer = (message, action) => {
    switch (action.type) {
        case 'miner cost':
        case 'storage upgrade':
        case 'hangar upgrade':
            return {
                type: action.type,
                data: action.data,
                storage: action.storage
            };
        default:
            return {type: null};
    }
}


