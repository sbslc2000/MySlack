import styled from "styled-components";
import SlackDefaultButton from "../../ui/SlackDefaultButton";
import ModalHeader from "./structure/ModalHeader";
import ModalBody from "./structure/ModalBody";
import ModalFooter from "./structure/ModalFooter";
import ModalFrame from "./structure/ModalFrame";



const ModalDialog = styled.div`
     --bs-modal-width: ${props => props.width}px;
`;
const ModalContent = styled.div`
    background-color: #1A1D21;
    color:white;
    padding-left:20px;
    padding-right:20px;
`;


const HeaderText = styled.h1`
    font-weight:900;
`;

function ErrorModal(props) {

    const width = 520;
    const height = 500;

    let modalHeader = (
        <ModalHeader>
            <HeaderText className="modal-title fs-5" id="exampleModalLabel"></HeaderText>
            <button type="button" className="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
        </ModalHeader>
    )

    let modalBody = (
        <ModalBody>
            <div>@ sbslc2000@gmail.com</div>
        </ModalBody>
    )

    let modalFooter = (
        <ModalFooter className="modal-footer">
            <SlackDefaultButton fontSize={15} width={180} height={36} title={"더 많은 사용자 초대"} color={"back"} type="button"  className="btn btn-primary"></SlackDefaultButton>
            <SlackDefaultButton fontSize={15} width={80} height={36} title={"완료됨"} color={"primary"} type="button" dataBsDismiss={"modal"} className="btn btn-primary"></SlackDefaultButton>
        </ModalFooter>
    )




    return (
        <ModalFrame width={width} id="error">
            {modalHeader}
            <div style={{color:"#C7C8C9"}}>
                {modalBody}
            </div>
            {modalFooter}
        </ModalFrame>
    );
}

export default ErrorModal;