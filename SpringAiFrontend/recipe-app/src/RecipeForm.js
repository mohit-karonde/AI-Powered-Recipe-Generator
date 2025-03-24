import React, { useState } from 'react';
import axios from 'axios';

function RecipeForm({ onRecipeGenerated }) {
  const [ingredients, setIngredients] = useState('');
  const [cuisine, setCuisine] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await axios.get('http://localhost:8080/recipes/generate', {
        params: {
          ingredients: ingredients,
          cuisine: cuisine,
        },
      });

      // Extract the recipe text from the response
      const recipeText = response.data.candidates[0].content.parts[0].text;
      onRecipeGenerated(recipeText);
    } catch (err) {
      setError('Failed to generate recipe. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>
          Ingredients:
          <input
            type="text"
            value={ingredients}
            onChange={(e) => setIngredients(e.target.value)}
            required
          />
        </label>
      </div>
      <div>
        <label>
          Cuisine:
          <input
            type="text"
            value={cuisine}
            onChange={(e) => setCuisine(e.target.value)}
            required
          />
        </label>
      </div>
      <button type="submit" disabled={loading}>
        {loading ? 'Generating...' : 'Generate Recipe'}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
}

export default RecipeForm;