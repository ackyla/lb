class AddValidToTerritory < ActiveRecord::Migration
  def self.up
    change_table :territories do |t|
      t.boolean :valid, :default => true
    end
  end

  def self.down
    change_table :territories do |t|
      t.remove :valid
    end
  end
end
