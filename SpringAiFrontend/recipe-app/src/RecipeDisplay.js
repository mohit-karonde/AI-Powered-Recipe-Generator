import React from 'react';

function RecipeDisplay({ recipe }) {
  return (
    <div className="recipe-display">
      <h2>Generated Recipe</h2>
      <p>{recipe}</p>
    </div>
  );
}

export default RecipeDisplay;