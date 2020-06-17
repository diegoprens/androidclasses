import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipesService } from '../recipes.service';
import { Recipe } from '../recipe.model';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.page.html',
  styleUrls: ['./recipe-detail.page.scss'],
})
export class RecipeDetailPage implements OnInit {

  currentRecipe:Recipe;

  constructor(private recipesService:RecipesService,
              private activatedRoute:ActivatedRoute,
              private router:Router) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe( paramMap => {
      if( !paramMap.has('recipeId') ){
        return;
      }
      const recipeId = paramMap.get('recipeId');
      this.currentRecipe = this.recipesService.getByRecipeId(recipeId);
    });
  }

  delete(){
    console.log('Deleting...');
    this.recipesService.deleteRecipe(this.currentRecipe.id);
    this.router.navigate(['./recipes']);
  }

}
