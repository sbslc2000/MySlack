import styled from "styled-components";
import SlackDefaultButton from "../../../ui/SlackDefaultButton";
import {useState} from "react";
import axios from "axios";


const ModalDialog = styled.div`
     --bs-modal-width: ${props => props.width}px;
`;
const ModalContent = styled.div`
    background-color: #1A1D21;
    color:white;
    padding-left:20px;
    padding-right:20px;
`;

const MessageTextarea = styled.textarea`
    background-color:inherit;
    color:inherit;
    border:1px solid #515357;
    height:44px;
    font-size:18px;
    
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

const HeaderText = styled.h1`
    font-weight:900;
`;

const ModalHeader = styled.div`
    padding-top:20px;
    padding-bottom:20px;
    border-bottom:0px;
    display:flex;
    flex-direction:column;
  
`;

const ModalBody = styled.div`
    border-bottom:0px;
    padding-top:0px;
`;

const InviteLinkCopyButton = styled.button`
    background-color: inherit;
    color:#1D9BD1;
    border:0;
`;

const ModalFooter = styled.div`
    border-top:0px;
    display:flex;
    justify-content:space-between;
`


function InviteWorkspaceModal(props) {
    const [progress,setProgress] = useState(0);


    const width = progress === 0 ? 650 : 520;
    const height = 500;

    const nextButtonHandler = () => {
        setProgress(1);
    };
    const closeHandler = () => {
        setProgress(0);
    }


    const backButtonHandler = () => {
        setProgress(0);
    }

    let modalHeader = progress === 0 ?
        (
            <ModalHeader  className="modal-header">
                <HeaderText className="modal-title fs-5" id="exampleModalLabel">{props.workspace.name}(으)로 사용자 초대</HeaderText>
                <button onClick={closeHandler} type="button" className="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </ModalHeader>
        ) : (
            <>
                <ModalHeader className="modal-header">
                    <button onClick={closeHandler} type="button" className="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    <HeaderText>✅</HeaderText>
                    <HeaderText style={{fontSize:18}} className="modal-title fs-5" id="exampleModalLabel">초대를 보냈습니다</HeaderText>
                </ModalHeader>
            </>
        );

    let modalBody = progress === 0 ? (
        <ModalBody className="modal-body" >
            <div style={{paddingLeft:5,paddingBottom:8,fontSize:15}}>
                 초대 받을 사람:
            </div>
            <form>
                <div className="form-group">
                    <MessageTextarea placeholder="name@gmail.com" className="form-control" id="textareaContent"
                                     rows="1"></MessageTextarea>
                </div>
            </form>
        </ModalBody>
    ): (
        <ModalBody className="modal-body" style={{borderBottom:0}}>
            <div>@ sbslc2000@gmail.com</div>
        </ModalBody>
    );


    let modalFooter = progress === 0 ? (
        <ModalFooter className="modal-footer" style={{}}>
            <InviteLinkCopyButton>
                @ 초대 링크 복사
            </InviteLinkCopyButton>
            <SlackDefaultButton fontSize={15} width={80} height={36} title={"다음"} color={"primary"} onClick={nextButtonHandler} type="button" data-bs-toggle="modal"  className="btn btn-primary"></SlackDefaultButton>
        </ModalFooter>
    ): (
        <div className="modal-footer" style={{borderTop:0}}>
            <SlackDefaultButton fontSize={15} width={180} height={36} title={"더 많은 사용자 초대"} color={"back"} onClick={backButtonHandler} type="button"  className="btn btn-primary"></SlackDefaultButton>
            <SlackDefaultButton fontSize={15} width={80} height={36} title={"완료됨"} color={"primary"} type="button" dataBsDismiss={"modal"} className="btn btn-primary"></SlackDefaultButton>
        </div>
    );
    return (
        <div  className="modal fade" id="inviteWorkspace" tabIndex="-1" aria-labelledby="exampleModalLabel"
              aria-hidden="true">
            <ModalDialog width={width} className="modal-dialog modal-dialog-centered">
                <ModalContent className="modal-content">
                    {modalHeader}
                    <div style={{color:"#C7C8C9"}}>
                        {modalBody}
                    </div>

                    {modalFooter}
                </ModalContent>
            </ModalDialog>
        </div>
    );
}

export default InviteWorkspaceModal;