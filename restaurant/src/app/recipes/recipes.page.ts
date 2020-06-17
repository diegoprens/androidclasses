import { Component, OnInit } from '@angular/core';
import { Recipe } from './recipe.model';
import { RecipesService } from './recipes.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.page.html',
  styleUrls: ['./recipes.page.scss'],
})
export class RecipesPage implements OnInit {

  recipes:Recipe[];

  constructor(
    private recipesService:RecipesService,
    private activeroute:ActivatedRoute) { }

  ngOnInit() {
    this.activeroute.paramMap.subscribe(paramMap => {
      this.recipes = this.recipesService.getAllRecipes();
    });
  }
}
