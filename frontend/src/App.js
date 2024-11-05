import AppRouter from './Router';
import MenuBar from './components/navigation/MenuBar';
import Footer from './components/navigation/Footer';
import './App.css';
//import AppRouter from './Router';
//import MenuBar from './components/MenuBar';

function App() {
  return (
    <>
      <MenuBar />
      <AppRouter />
      <Footer />
    </>
  );
}

export default App;
