import React, { useState } from 'react';
import RecipeForm from './RecipeForm';
import RecipeDisplay from './RecipeDisplay';
import './App.css';

function App() {
  const [recipe, setRecipe] = useState('');

  const handleRecipeGenerated = (generatedRecipe) => {
    setRecipe(generatedRecipe);
  };

  return (
    <div className="App">
      <h1>Recipe Creator</h1>
      <RecipeForm onRecipeGenerated={handleRecipeGenerated} />
      {recipe && <RecipeDisplay recipe={recipe} />}
    </div>
  );
}

export default App;
