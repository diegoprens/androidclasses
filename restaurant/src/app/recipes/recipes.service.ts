import { Injectable } from '@angular/core';
import { Recipe } from './recipe.model';

@Injectable({
  providedIn: 'root'
})
export class RecipesService {

  constructor() { }

  recipes:Recipe[] = [
    {
      id:'r1',
      title: 'Bandeja Paisa',
      imageUrl: "https://comidatipicadecolombia.top/wp-content/uploads/2018/07/Bandeja-Paisa.jpg",
      ingredients: ['Frijol', 'Arroz', "Platano Maduro"]
    },
    {
      id:'r2',
      title: 'Costillas a la Barbecue',
      imageUrl: 'https://sevilla.abc.es/gurme/wp-content/uploads/sites/24/2009/06/costillas-de-cerdo-barbacoa-960x540.jpeg',
      ingredients: ['1 costilla de cerdo', 'kétchup', 'Vinagre']
    }
  ];

  getAllRecipes(){
    return [...this.recipes];
  }

  getByRecipeId(recipeId){
    return {...this.recipes.find(recipe => recipe.id == recipeId)};
  }

  deleteRecipe(recipeId:string){
    this.recipes = this.recipes.filter(recipe => recipe.id !== recipeId);
  }

}
