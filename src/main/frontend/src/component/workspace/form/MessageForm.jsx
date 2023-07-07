import styled from "styled-components";
import {useContext, useState} from "react";
import axios from "axios";
import {useLocation} from "react-router-dom";
import {ChannelContext} from "../../../page/WorkspacePage";

const Wrapper = styled.div`
    padding-left:20px;
    padding-right:20px;
    height:142px;
    position:absolute;
    width:1660px;
    bottom:0;

`;

const MessageFormWrapper = styled.div`
    border: 1px solid #818385;
    border-radius: 4px;
    height:118px;
    background-color:#222529;
`;

const MessageTextarea = styled.textarea`
    background-color:inherit;
    color:inherit;
    border:0;
    
    
    resize:none;
    
    &::placeholder{
        color:#7A7B7D;
        font-weight:900;
    }
    &:focus {
        background-color: #222529;
        color:white;
        border:0;
        box-shadow:none;
    }
`;

const MessageFormButtonBar = styled.div`
    display:flex;
    justify-content:${props => props.justifyContent}left;
    height:30px;
    margin:4px;
    padding-left:4px;
`;

const MessageFormButton = styled.button`
    background-color:inherit;
    height:30px;
    width:30px;
    border:0;
    color:#4C4E51;
    display:flex;
    align-items:center;
    
    &:hover {
        background-color:#2A2D30;
    }
`;

const SubmitButton = styled.button`
    background-color:#007A5A;
    color:white;
    border:0;
    height:28px;
    width:32px;
    border-radius:4px;
    display:flex;
    align-items:center;
    > svg {
        width:17px;
        height:17px;
    }
    
    &:hover {
        background-color:#0C8062;
    }
`;

function MessageForm() {

    const [message, setMessage] = useState("");

    let location = useLocation();
    let workspaceId = location.pathname.split("/")[3];
    let channelState = useContext(ChannelContext);
    let channelId = channelState.currentChannel.id;
    const channelName = channelState.currentChannel.name;
    const formPlaceHolder = "#"+channelName+"에 메시지 보내기";

    const onChangeHandler = (e) => {
        setMessage(e.target.value);
    }

    const onSubmitHandler = (e) => {
        e.preventDefault();
        console.log(message);
        setMessage("");
        sendMessageRequest();
    }

    const sendMessageRequest = () => {
        console.log("send message request");
        axios.post("/api/workspaces/"+workspaceId+"/channels/"+channelId+"/messages", {
            content: message
        }).then((response) => {
            console.log(response.data)
        }).catch((error) => {
            console.log(error);
        });
    }

    return (
        <Wrapper>
            <MessageFormWrapper>
                <MessageFormButtonBar>
                    <MessageFormButton>
                        <svg data-nwn="true" aria-hidden="true" viewBox="0 0 20 20" className="">
                            <path fill="currentColor" fill-rule="evenodd"
                                  d="M4 2.75A.75.75 0 0 1 4.75 2h6.343a3.908 3.908 0 0 1 3.88 3.449A2.21 2.21 0 0 1 15 5.84l.001.067a3.901 3.901 0 0 1-1.551 3.118A4.627 4.627 0 0 1 11.875 18H4.75a.75.75 0 0 1-.75-.75V9.5a.75.75 0 0 1 .032-.218A.75.75 0 0 1 4 9.065V2.75Zm2.5 5.565h3.593a2.157 2.157 0 1 0 0-4.315H6.5v4.315Zm4.25 1.935H6.5v5.5h4.25a2.75 2.75 0 1 0 0-5.5Z"
                                  clip-rule="evenodd"></path>
                        </svg>
                    </MessageFormButton>
                    <MessageFormButton>
                        <svg data-nwn="true" aria-hidden="true" viewBox="0 0 20 20" className="">
                            <path fill="currentColor" fill-rule="evenodd"
                                  d="M7 2.75A.75.75 0 0 1 7.75 2h7.5a.75.75 0 0 1 0 1.5H12.3l-2.6 13h2.55a.75.75 0 0 1 0 1.5h-7.5a.75.75 0 0 1 0-1.5H7.7l2.6-13H7.75A.75.75 0 0 1 7 2.75Z"
                                  clip-rule="evenodd"></path>
                        </svg>
                    </MessageFormButton>
                    <MessageFormButton>
                        <svg data-nwn="true" aria-hidden="true" viewBox="0 0 20 20" className="">
                            <path fill="currentColor" fill-rule="evenodd"
                                  d="M11.721 3.84c-.91-.334-2.028-.36-3.035-.114-1.51.407-2.379 1.861-2.164 3.15C6.718 8.051 7.939 9.5 11.5 9.5a.76.76 0 0 1 .027.001h5.723a.75.75 0 0 1 0 1.5H2.75a.75.75 0 0 1 0-1.5h3.66c-.76-.649-1.216-1.468-1.368-2.377-.347-2.084 1.033-4.253 3.265-4.848l.007-.002.007-.002c1.252-.307 2.68-.292 3.915.16 1.252.457 2.337 1.381 2.738 2.874a.75.75 0 0 1-1.448.39c-.25-.925-.91-1.528-1.805-1.856Zm2.968 9.114a.75.75 0 1 0-1.378.59c.273.64.186 1.205-.13 1.674-.333.492-.958.925-1.82 1.137-.989.243-1.991.165-3.029-.124-.93-.26-1.613-.935-1.858-1.845a.75.75 0 0 0-1.448.39c.388 1.441 1.483 2.503 2.903 2.9 1.213.338 2.486.456 3.79.135 1.14-.28 2.12-.889 2.704-1.753.6-.888.743-1.992.266-3.104Z"
                                  clip-rule="evenodd"></path>
                        </svg>
                    </MessageFormButton>
                </MessageFormButtonBar>
                <form>
                    <div className="form-group">
                        <MessageTextarea  onChange={onChangeHandler} value={message} placeholder={formPlaceHolder} className="form-control" id="textareaContent"
                                         rows="1"></MessageTextarea>
                    </div>
                </form>
                <MessageFormButtonBar>
                    <SubmitButton onClick={onSubmitHandler} style={{position:"absolute",right:30}}>
                        <svg data-nwn="true" aria-hidden="true" viewBox="0 0 20 20" className="" >
                            <path fill="currentColor"
                                  d="M1.5 2.25a.755.755 0 0 1 1-.71l15.596 7.807a.73.73 0 0 1 0 1.306L2.5 18.46l-.076.018a.749.749 0 0 1-.924-.728v-4.54c0-1.21.97-2.229 2.21-2.25l6.54-.17c.27-.01.75-.24.75-.79s-.5-.79-.75-.79l-6.54-.17A2.253 2.253 0 0 1 1.5 6.789v-4.54Z"></path>
                        </svg>
                    </SubmitButton>
                </MessageFormButtonBar>
            </MessageFormWrapper>
        </Wrapper>
    )
}

export default MessageForm;