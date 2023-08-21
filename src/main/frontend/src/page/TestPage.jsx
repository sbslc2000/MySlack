import styled from "styled-components";

const Wrapper = styled.div`
  width:100vw;
  height:100vh;
  display: flex;
  flex-direction: column;
`;

const Header = styled.div`
  height:60px;
  background-color:black;
`;

const Section = styled.div`
  flex:1;
  background-color:gray;
`;
const TestPage = () => {
    return (
        <Wrapper>
            <Header>Test Page</Header>
            <Section>Test Section</Section>
        </Wrapper>
    )
}

export default TestPage;