class CreateCharacters < ActiveRecord::Migration
  def self.up
    create_table :characters do |t|
      t.string :name, null: false
      t.float :radius, default: 0.0
      t.float :precision
      t.timestamps
    end
  end

  def self.down
    drop_table :characters
  end
end
