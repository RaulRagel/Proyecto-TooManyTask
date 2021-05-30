start-db:
	@docker run --rm -d -p 5432:5432 \
		-e POSTGRES_DB=practicas -e POSTGRES_USER=practicas -e POSTGRES_PASSWORD=pr4ct1c4as \
		-v practicas-db-data:/var/lib/postgresql/data \
		--name practicas_db postgres:12-alpine

stop-db:
	@docker stop practicas_db

clean-db:
	@docker volume rm practicas-db-data