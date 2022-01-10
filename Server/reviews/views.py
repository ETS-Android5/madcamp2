from django.shortcuts import render

def add_review(request):
    if request.method == 'POST':
        print('hello')
