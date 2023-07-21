import styled from 'styled-components';
import SeparationLine from "./SeparationLine";
import DefaultUserIcon from "../../../../img/default_user.png";
import MessageWithName from "./MessageWithName";
import {useLocation} from "react-router-dom";
import axios from "axios";
import {useContext, useEffect, useState} from "react";
import MessageWithoutName from "./MessageWithoutName";
import {ChannelContext, RefreshContext} from "../../../../page/WorkspacePage";
import DateUtil from "../../../../util/DateUtil";
import {getMessages} from "../../../../api/message";

const Wrapper = styled.div`
    min-height:470px;
    max-height:470px;
    overflow:auto;
`;

const MessageWithNameWrapper = styled.div`
    padding-left:20px;
    padding-right:20px;
    display:flex;
    &:hover{
        background-color:#222529;
    }
    padding-top:16px;
`;

const MessageWithoutNameWrapper = styled.div`
    padding-top:5px;
    padding-left:66px;
    padding-right:20px;
    display:flex;
    &:hover{
        background-color:#222529;
    }
`;

const MessageUserIcon = styled.img`
    width: 36px;
    height: 36px;
    border-radius:5px;
`;

const MessageBodyWithName = styled.div`
    width: 1584px;
    display:flex;
    flex-direction:column;
    padding-left:10px;
    vertical-align:middle;
    
     &:hover{
        background-color:#222529;
    }
`;

const MessageBody = styled.div`
    font-size:16px;
    display:inline-block;
    > span {
         white-space: pre-wrap;
          word-break: break-all;
    }
`;


function MessageSection(props) {
    const channelState = useContext(ChannelContext);
    let refreshState = useContext(RefreshContext);

    let location = useLocation();
    let workspaceId = location.pathname.split("/")[3];
    let channelId = channelState.currentChannel.id;
    const [messages, setMessages] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        getMessages(channelId).then((messages) => {
            setMessages(messages);
            setIsLoading(false);
        });

        refreshState.setMessagesRefresh(false);
    }, [channelState.currentChannel,refreshState.messagesRefresh]);


    let prevSender = {};
    let prevDate = null;
    let content = <>
        {messages.map((message) => {
            let separationAddFlag = false;
            let currentDate = new Date(message.createdAt);

            if(prevDate === null || DateUtil.compareDate(prevDate,currentDate) !== 0) {
                separationAddFlag = true;
            }

            prevDate = currentDate;

            if(prevSender.id !== message.sender.id){

                prevSender = message.sender;
                return (
                <>
                    {separationAddFlag && <SeparationLine date={DateUtil.getDateKr(currentDate)}></SeparationLine>}
                    <MessageWithName message={message}/>
                </>
                );
            } else {
                prevSender = message.sender;
                return (<>
                        {separationAddFlag && <SeparationLine date={DateUtil.getDateKr(currentDate)}></SeparationLine>}
                        <MessageWithoutName message={message}/>
                </>

                );
            }
        })}
    </>;

    if(isLoading){
        content = <></>;
    }

    return (
        <Wrapper>
            {content}
        </Wrapper>
    );
}

export default MessageSection;