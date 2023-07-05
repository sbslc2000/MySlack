import styled from "styled-components";

const Wrapper = styled.div`
    width:100%;
    margin: 0 auto;
    display:flex;
    flex-direction:column;
    align-items: center;
`
function DefaultWrapper(props) {

    return (
        <Wrapper>
            {props.children}
        </Wrapper>
    );
}

export default DefaultWrapper;