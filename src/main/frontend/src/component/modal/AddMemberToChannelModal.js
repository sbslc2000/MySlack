import ModalFrame from "./structure/ModalFrame";
import ModalHeader from "./structure/ModalHeader";
import ModalBody from "./structure/ModalBody";
import ModalFooter from "./structure/ModalFooter";

const AddMemberToChannelModal = (props) => {

    return (
        <ModalFrame width={500} id="addMemberToChannel">
            <ModalHeader></ModalHeader>
            <ModalBody></ModalBody>
            <ModalFooter></ModalFooter>
        </ModalFrame>
    );
}

export default AddMemberToChannelModal;